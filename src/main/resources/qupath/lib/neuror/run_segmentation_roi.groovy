import ij.plugin.filter.ThresholdToSelection
import ij.process.ImageProcessor
import qupath.lib.objects.PathObjects
import qupath.lib.regions.ImagePlane
import qupath.imagej.processing.RoiLabeling
import ij.IJ
import static qupath.lib.gui.scripting.QPEx.*
import java.awt.image.BufferedImage

import qupath.lib.images.servers.ImageServers

// --- SET THESE PARAMETERS -----------------------------------------------------------
def anacondaEnvPath = "%s"
def pythonExecPath = "%s"
def execute_file = "%s"

// Please enter the absolute paths for the model, the original image, and the output directory for the TIFF files
def model_path = "%s"
//def image_path = "E:/NeuroR_Seg_Qupath/original_images/digital_pathology_svs_files/"
def image_path = "%s"
def output_path = "%s"

// Input name of your annotation (geojson) folder
json_export_folder = "%s"

def masksPath = output_path // path to where TIFFs are stored 

def imageData = getCurrentImageData();
image_name = imageData.getServer().getMetadata().getName()

// Image processing library
def img_lib = "%s" // or "bioformats" // or "dicom"

// If importing DICOM, set to true
//boolean is_dicom = false
if (img_lib == "dicom")
{
    image_name = image_name.split('-')[0].trim()

}

int patch_size = %s
int level = %s                    // which level to extract segmentation from (choosing 0 may be slow)
def extension = ".tiff"                 // pyramidal TIFF
//def classNames = ["Epidermis", "Adnexa"]    // names of classes of interest (in this case two classes, excluding the background class)
// classNames = ["Epidermis", "Adnexa"]
classNames = [%s]
def channel = 0// 0-based index for the channel to threshold
int overlap = %s

int batch_size = %s // The unit of the number of images during inference ex) 4, 16, 32, 64..
def dev_mode = "gpu" // or "cpu"
def if_config = "false" // or "true" select between 32-bit and 16-bit floating points
int num_gpus = %s

// ------------------------------------------------------------------------------------
def selectedClassNames = [%s]

// ------------------------------------------------------------------------------------

// --- Export Json Annotations --------------------------------------------------------

// // export_focus_geojson .. etc
export_extension= ".geojson"
// .geojson

// Get annotations
//def annotations = getAnnotationObjects().collect {new qupath.lib.objects.PathAnnotationObject(it.getROI(), it.getPathClass())}
def annotations = getAnnotationObjects().findAll { annotation ->
    selectedClassNames.any { className -> annotation.getPathClass().getName() == className } && annotation.getROI()
}
// Get Gson instance
def gson = GsonTools.getInstance(true)

// Create 'annotations' directory if doesn't exist
def path = buildFilePath(json_export_folder)


def filename = GeneralTools.getNameWithoutExtension(getProjectEntry().getImageName())

if (img_lib == "dicom")
{  
    filename = filename.split('-')[0].trim()
    filename = filename.substring(0, filename.lastIndexOf('.'))
}

new File(path).mkdir()

// Write to a new file inside the 'annotations' directory
File file = new File(path + "/" + filename + export_extension)
file.write(gson.toJson(annotations))

def json_path = path + "/" + filename + export_extension

print 'exported!'

// --- RUN PYTHON MODULE --------------------------------------------------------------

def command = [pythonExecPath, execute_file, model_path, json_path, image_path, image_name, output_path, patch_size as String, level as String, classNames as String, batch_size as String, overlap as String, dev_mode, if_config as String, img_lib, num_gpus as String]
def processBuilder = new ProcessBuilder(command)
processBuilder.directory(new File("."))
processBuilder.environment().put("PATH", anacondaEnvPath + ";" + System.env.PATH)
processBuilder.redirectErrorStream(true)
def process = processBuilder.start()
def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
def downsample = ""
reader.eachLine {
    println it
    if (it.contains("Downsample"))
        downsample = it.split(':')[1].trim().toInteger()
}
process.waitFor()
print "Inference Done"
//
//// ------------------------------------------------------------------------------------
//
//// --- ANNOTATION IMPORT --------------------------------------------------------------
//
// Get a list of image files, stopping early if none can be found\

def dirOutput = new File(masksPath);
// print(dirOutput)
if (!dirOutput.isDirectory()) {
    print dirOutput + ' is not a valid directory!';
    return;
}

// get current WSI, update paths to file
def currWSIName = GeneralTools.getNameWithoutExtension(getProjectEntry().getImageName())

if (img_lib == "dicom")
{  
    currWSIName = currWSIName.split('-')[0].trim()
    currWSIName = currWSIName.substring(0, currWSIName.lastIndexOf('.'))
}

def path2 = ""
String[] paths = new String[classNames.size()]

for (int i=0; i< classNames.size(); i++)
{
   currClassName = classNames[i]
   selectObjectsByClassification(currClassName); 
}

for (int i=0; i< classNames.size(); i++)
{

   paths[i] = masksPath + "/" + currWSIName +  "_" +classNames[i] + extension 
   path2 = paths[i]
   currClassName = classNames[i]
   // check if file exists, if no return
   File file2 = new File(path2)
   if (!file2.exists()) {
       print path2 + ' does not exist!';
       return;
   }
   print(path2)
   def server = buildServer(path2)
   //   if (level != 0) {
   //       server = qupath.lib.images.servers.ImageServers.pyramidalize(server, server.getDownsampleForResolution(level))
   //   }
   def belowClass = getPathClass('Ignore*')     // Class for pixels below the threshold
   def current_thresh = 0.5;
     
   def aboveClass = getPathClass(currClassName)     // Class for pixels above the threshold
   
   // Create a thresholded image
   def thresholdServer = PixelClassifierTools.createThresholdServer(server, channel, current_thresh, belowClass, getPathClass(currClassName))
   def hierarchy = getCurrentHierarchy()
   PixelClassifierTools.createAnnotationsFromPixelClassifier(hierarchy, thresholdServer, -1, -1)

   // Select current annotations
   
   selectObjectsByClassification(currClassName);
   def oldObjects = getAnnotationObjects().findAll{it.getPathClass() == getPathClass(currClassName)}
   def transform = java.awt.geom.AffineTransform.getScaleInstance(downsample, downsample)
   def newObjects = oldObjects.collect {p -> PathObjectTools.transformObject(p, transform, false)}
   
   // Delete old annotations
   clearSelectedObjects(false);

   addObjects(newObjects)
//    getCurrentViewer().repaint()

   current_thresh++;

}

print "Done: " + currWSIName;


Thread.sleep(100);
javafx.application.Platform.runLater {
getCurrentViewer().getImageRegionStore().cache.clear();
System.gc();
}
Thread.sleep(100);