package qupath.ext.neuror

import qupath.lib.roi.ROIs
import qupath.lib.roi.RectangleROI
import qupath.lib.regions.ImagePlane
import qupath.lib.objects.PathObjects
import qupath.lib.gui.dialogs.Dialogs
import qupath.lib.objects.PathAnnotationObject
import qupath.lib.plugins.objects.SmoothFeaturesPlugin

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Set parameters(Please enter the absolute path & options related to patches)

// Parameters for running Python and Anaconda
String anacondaEnvPath = "%s"
String pythonExecPath = "%s"
String execute_file = "%s"

// Please enter the absolute paths for the model, the original image, and the output directory for the TIFF files
String imgPath = "%s"
String modelPath = "%s"
String outputPath =  "%s"

// Input name of your annotation (geojson) folder
String exportJsonPath = "%s"

// Image Read/Write library options
String imgLib = "%s"                    // opt : ["openslide", "bioformats", "others"]

// Device options
int numGPUs = %s
String devMode = "gpu"                         // opt : ["gpu", "cpu"]
boolean fp16Flag = true                        // opt : [true, false] - Calculate 16-bit or 32-bit floating points


//------------------------------------------------------------------------
// Annotation options(Qupath)
ArrayList selectedClassNames = [%s] // [] [""] for all region (with generated "Region*" class)

//-----------------------------------------------------------------------

// Patch mode options
int level = %s                               // which level to extract segmentation from (choosing 0 may be slow)
int overlap = %s
int batchSize = %s                              // The unit of the number of images during inference ex) 4, 16, 32, 64..
int patchSize = %s

// Prob threshold by Class
boolean probThreshFlag = false                 // opt : [true, false] 
ArrayList probThreshList = [0.8, 0.8]

// Grade priority options
boolean gradeMode = %s                       // opt : [true, false] - true: gradual data / false: binary or multiclass data
ArrayList grades = [%s]
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


// Func 1. Export Annotations information ////////////////////////////////////////////////////////////////////////
println("=== Export json file Start! ===")
String jsonExt = ".geojson"
String imgName = getProjectEntry().getImageName()

// Get annotations 
def isAnno = getAnnotationObjects().findAll()

def annotations = getAnnotationObjects().findAll { annotation ->
    def pathClass = annotation.getPathClass()
    // Check if pathClass is not null and getName() is one of the selected class names
    pathClass && selectedClassNames.any { className -> pathClass.getName() == className } && annotation.getROI()
}

if (!isAnno || !annotations) {
    def imageData = getCurrentImageData()
    def server = imageData.getServer()
    width = server.getWidth().toInteger()
    height = server.getHeight().toInteger()
    
    def roi = new RectangleROI(0, 0, width, height)
    def wholeAnno = new PathAnnotationObject(roi, PathClassFactory.getPathClass("Region*"))
    addObject(wholeAnno)

    annotations = getAnnotationObjects()
}

// Get gson instance
def gson = GsonTools.getInstance(true)

// Create 'annotations' directory if doesn't exist
Path jsonPath = Paths.get(exportJsonPath)
try {
    // This will create the directory and all non-existent parent directories
    if (!Files.exists(jsonPath)) { Files.createDirectories(jsonPath); }
} catch (IOException e) {
    println("ERROR: Unable to create directory at ${exportJsonPath}")
    e.printStackTrace()
}



// Write to a new file inside the 'annotations' directory
try {
    Path path = Paths.get(exportJsonPath, GeneralTools.stripExtension(imgName) + jsonExt)
    Files.write(path, gson.toJson(annotations).getBytes())
    exportJsonPath = path.toString()
    println("Save path : ${path}")
    println("=== Export json file successful! ===\n")
} catch (IOException e) {
    e.printStackTrace();
    println("=== Export json file failed! ===\n")
}


// // --- Func 2. RUN PYTHON MODULE --------------------------------------------------------------
ArrayList command = []
command.addAll(pythonExecPath,
                execute_file,
                modelPath,
                imgPath,
                imgName,
                imgLib,
                exportJsonPath,
                outputPath,
                devMode,
                numGPUs as String,
                fp16Flag as String,
                level as String,
                overlap as String,
                batchSize as String,
                patchSize as String,
                probThreshFlag as String,
                probThreshList as String,
                gradeMode as String,
                grades as String
                )

ProcessBuilder processBuilder = new ProcessBuilder(command)
processBuilder.directory(new File("."))
processBuilder.environment().put("PATH", anacondaEnvPath + ";" + System.env.PATH)
processBuilder.redirectErrorStream(true)

println("=== Inference Start! ===")
int downsample = 0
Process process = processBuilder.start()

BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
errorReader.eachLine {
    print it
}
reader.eachLine {
    println it
    if (it.contains("Downsample"))
        downsample = it.split(':')[1].trim().toInteger()
}

process.waitFor()
process.destroy()
println("=== Inference Finished! ===\n")


// --- Func 3. Read CSV & Classification Mapping --------------------------------------------------------------
def interpolateColor(float[] probs) {
    float red, green, blue
 
    red = 255 * probs[0] * 10
    green = 0
    blue = 255 * probs[1]
 
    return new int[] {red, green, blue}
}

// Set opacity
getCurrentViewer().getOverlayOptions().setOpacity(0.3)

// Clear objects
var toDelete = getDetectionObjects().findAll{it.getROI().getArea()}
getCurrentHierarchy().removeObjects(toDelete, false)

// Set csv file path
String csvPath = Paths.get(outputPath, GeneralTools.stripExtension(imgName) + ".csv").toString()

// Create a CSV reader
try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
    float value
    int i, x, y, width, height

    String[] headers
    String DELIMITER = ","
    String line, valuename
    
    // read CSV header line
    if ((line = br.readLine()) != null) {
        headers = line.split(DELIMITER)
        if (headers.length < 5 ||
            ! headers[0].equals("x") || ! headers[1].equals("y") ||
            ! headers[2].equals("width") || ! headers[3].equals("height")) {
            Dialogs.showErrorMessage("CSV error", "The CSV header line is invalid; should be 'x,y,width,height,VALUENAME1,...'")
            return
        }      
    } 
    else {
        Dialogs.showErrorMessage("CSV error", "The CSV file is empty")
        return
    }

    // Add Objects
    int linenum = 0
    ArrayList detections = []
    float[] probs = new float[headers.length - 5]
 
    while ((line = br.readLine()) != null) {
        linenum++
        String[] cols = line.split(DELIMITER)
        if (cols.length != headers.length) {
            Dialogs.showErrorMessage("CSV file error", "CSV file line " + linenum + " has incorrect number of fields")
            break
        }

        // Read values by columns
        x = Integer.parseInt(cols[0])
        y = Integer.parseInt(cols[1])
        width = Integer.parseInt(cols[2])
        height = Integer.parseInt(cols[3])
        pred = Integer.parseInt(cols[4])
        pathclassName = headers[5+pred].replace(" prob", "")
        for (i = 5; i < cols.length; i++) {
            probs[i - 5] = Float.parseFloat(cols[i])
        }
        roi = ROIs.createRectangleROI(x, y, width, height, ImagePlane.getPlane(0, 0))

        // Add detection
        int[] resultRGB = interpolateColor(probs)

        pathclass0 = PathClass.fromString(pathclassName)
        detection = PathObjects.createDetectionObject(roi, pathclass0)
        detection.setColor(getColorRGB(resultRGB[0],resultRGB[1],resultRGB[2]))
        for (i = 5; i < cols.length; i++){    
            detection.measurements.put(headers[i], probs[i-5])
        }
        detections.add(detection)
    }
    addObjects(detections)
   
    // Apply probability heatmap
    float fwhmPixels = 200.0
    String fwhmString = null
    boolean withinClass = false
    boolean useLegacyNames = false

    for (i = 5; i < headers.length; i++){
        ArrayList classProbList = [headers[i]]
        SmoothFeaturesPlugin.smoothMeasurements(detections, classProbList, fwhmPixels, fwhmString, withinClass, useLegacyNames)
    }

} catch (IOException ex) {
    Dialogs.showErrorMessage("File open error", ex.getMessage())
}

println("=== All process Finished! ===")