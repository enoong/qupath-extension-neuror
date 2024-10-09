package qupath.ext.neuror

import qupath.lib.objects.PathObjects
import qupath.lib.roi.ROIs
import qupath.lib.regions.ImagePlane
import qupath.lib.gui.dialogs.Dialogs
import qupath.lib.plugins.objects.SmoothFeaturesPlugin

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

import qupath.lib.roi.RectangleROI
import qupath.lib.objects.PathAnnotationObject

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Set parameters(Please enter the absolute path & options related to patches)

String anacondaEnvPath = "%s"

String pythonExecPath = "%s"
String imgPath = "%s"
String modelPath = "%s"
String outputPath =  "%s"
String exportJsonPath = "%s"
 
// Image Read/Write library options
String imgLib = "%s"                    // opt : ["bioformats", "openslide", "else"]

// Device options
int numGPUs = %s
String devMode = "gpu"                         // opt : ["gpu", "cpu"]
boolean fp16Flag = true                        // opt : [true, false] - Calculate 16-bit or 32-bit floating points

//------------------------------------------------------------------------
// Annotation options(Qupath)
ArrayList selectedClassNames = [%s]
ArrayList claClassNames = [%s]
//------------------------------------------------------------------------

// Patch mode options
int level = %s                                  // which level to extract segmentation from (choosing 0 may be slow)
int overlap = %s
int batchSize = %s                              // The unit of the number of images during inference ex) 4, 16, 32, 64..
int patchSize = %s

// Prob threshold by Class
boolean probThreshFlag = false                 // opt : [true, false]
ArrayList probThreshList = [1.0, 1.0]

// Grade priority options
boolean gradeMode = %s                      // opt : [true, false] - true: gradual data / false: binary or multiclass data
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
command.addAll(anacondaEnvPath + "/python",
                pythonExecPath,
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

Process process = processBuilder.start()

BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
errorReader.eachLine {
    print it
}
reader.eachLine {
    println it
}

println("=== Inference Finished! ===")
process.destroy()

// --- Func 3. Read CSV & Classification Mapping --------------------------------------------------------------
// Set opacity
getCurrentViewer().getOverlayOptions().setOpacity(0.3)
 
// Set csv file output path
 
 
// create a reader
 
// int[] interpolateColor(int pred, float[] probs) {
def interpolateColor(int pred, float[] probs){
   
    float red;
    float green;
    float blue;
 
    red = 255* probs[0]* 10;
    green = 0;
    blue = 255* probs[1];
 
    return new int[]{red, green, blue};
 
}

def detections = []
def csvfile = outputPath + GeneralTools.stripExtension(imgName) + '.csv'
 
try (BufferedReader br = new BufferedReader(new FileReader(csvfile))) {
   
 
    // CSV file delimiter
    String DELIMITER = ","
    String line, valuename
    String[] headers
   
    int i, x, y, width, height
    float value
   
    // read CSV header line
    if ((line = br.readLine()) != null) {
        headers = line.split(DELIMITER)
 
        if (headers.length < 5 ||
            ! headers[0].equals("x") || ! headers[1].equals("y") ||
            ! headers[2].equals("width") || ! headers[3].equals("height")) {
            Dialogs.showErrorMessage("CSV error", "The CSV header line is invalid; should be 'x,y,width,height,VALUENAME1,...'")
            return
        }      
    } else {
        Dialogs.showErrorMessage("CSV error", "The CSV file is empty")
        return
    }
   
    // Clear Objects
    var toDelete = getDetectionObjects().findAll{it.getROI().getArea()}
    getCurrentHierarchy().removeObjects(toDelete, false)
   
    // Add Objects
    int linenum = 0
 
    float[] probs = new float[headers.length - 5]
 
    while ((line = br.readLine()) != null) {
        linenum++
        String[] columns = line.split(DELIMITER)
        if (columns.length != headers.length) {
            Dialogs.showErrorMessage("CSV file error", "CSV file line " + linenum + " has incorrect number of fields")
            break
        }
        // Read values by columns
//        x = Integer.parseInt(columns[0]) - (patchSize + downsample) * downsample / 2
//        y = Integer.parseInt(columns[1]) - (patchSize + downsample) * downsample / 2
        x = Integer.parseInt(columns[0])
        y = Integer.parseInt(columns[1])
       
        // 5) patch overlap 타일로 뿌려주기
        // width = patchSize * downsample 
        // height = patchSize * downsample
        width = Integer.parseInt(columns[2])
        height = Integer.parseInt(columns[3])
 
        pred = Integer.parseInt(columns[4])
 
        for (i = 5; i < columns.length; i++) {
            probs[i - 5] = Float.parseFloat(columns[i])
        }
       
        roi = ROIs.createRectangleROI(x, y, width, height, ImagePlane.getPlane(0, 0))
        // Add detection
        int[] resultRGB = interpolateColor(pred, probs)
 
 
        // 2) object별 색상 적용
        pathclass0 = PathClass.fromString(claClassNames[pred].toString())
        detection = PathObjects.createDetectionObject(roi, pathclass0)
        detection.setColor(getColorRGB(resultRGB[0],resultRGB[1],resultRGB[2]))
        // detection.setColor(getColorRGB(0,0,0))
        // 3) Prob measurement 추가 (patch별 prob 보이게)
        for (i = 5; i < columns.length; i++){    
            detection.measurements.put(headers[i], probs[i-5])
        }
 
        detections.add(detection)
 
    }
    addObjects(detections)
   
 
    // 1) Prob Heatmap 구현
    def fwhmPixels = 200.0 // smoothing 정도
    def fwhmString = null
    def withinClass = false // 클래스 내부에서만 처리할 것인지 아니면 모든 객체를 대상으로 할 것인지
    def useLegacyNames = false
 
    for (i = 5; i < headers.length; i++){
        // def classProbList = (1..(detections.size() - 2)).collect { headers[i] }
        def classProbList = [ headers[i] ]
        SmoothFeaturesPlugin.smoothMeasurements(detections, classProbList, fwhmPixels, fwhmString, withinClass, useLegacyNames)
    }
 
 
} catch (IOException ex) {
    Dialogs.showErrorMessage("File open error", ex.getMessage())
}


print "=== All process Finished! ==="

