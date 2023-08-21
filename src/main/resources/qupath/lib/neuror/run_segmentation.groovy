package qupath.lib.neuror

import ij.plugin.filter.ThresholdToSelection
import ij.process.ImageProcessor
import qupath.lib.objects.PathObjects
import qupath.lib.regions.ImagePlane
import qupath.imagej.processing.RoiLabeling
import ij.IJ
import static qupath.lib.gui.scripting.QPEx.*
import java.awt.image.BufferedImage

// --- SET THESE PARAMETERS -----------------------------------------------------------

// Parameters for running Python and Anaconda
def anacondaEnvPath = "%s"
def pythonExecPath = "%s"
def execute_file = "%s"

// Please enter the absolute paths for the model, the original image, and the output directory for the TIFF files
def model_path = "%s"
def image_path = "%s"
def output_path = "%s"

def masksPath = output_path // path to where TIFFs are stored 

def imageData = getCurrentImageData();
image_name = imageData.getServer().getMetadata().getName()

// Image processing library
def img_lib = "%s" // "openslide" // or "bioformats" // or "dicom"

// If importing DICOM, set to true
//boolean is_dicom = false
if (img_lib == "dicom")
{
    image_name = image_name.split('-')[0].trim()

}

int patch_size = %s
int level = %s                     // which level to extract segmentation from (choosing 0 may be slow)
def extension = ".tiff"                 // pyramidal TIFF
//def classNames = ["Epidermis", "Adnexa"]    // names of classes of interest (in this case two classes, excluding the background class)
classNames = [%s]
def channel = 0// 0-based index for the channel to threshold
int overlap = %s

int batch_size = %s // The unit of the number of images during inference ex) 4, 16, 32, 64..
def dev_mode = "gpu" // or "cpu"
def if_config = "false" // or "true" select between 32-bit and 16-bit floating points
int num_gpus = %s

// ------------------------------------------------------------------------------------


// --- RUN PYTHON MODULE --------------------------------------------------------------

def command = [pythonExecPath, execute_file, model_path, image_path, image_name, output_path, patch_size as String, level as String, classNames as String, batch_size as String, overlap as String, dev_mode, if_config as String, img_lib, num_gpus as String]
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

//
//// ------------------------------------------------------------------------------------
//
//// --- ANNOTATION IMPORT --------------------------------------------------------------
//
// Get a list of image files, stopping early if none can be found\

def dirOutput = new File(masksPath);
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

def path = ""
String[] paths = new String[classNames.size()]

for (int i=0; i< classNames.size(); i++)
{

   paths[i] = masksPath + "/" + currWSIName +  "_" +classNames[i] + extension 
   path = paths[i]
   currClassName = classNames[i]
   // check if file exists, if no return
   File file = new File(path)
   if (!file.exists()) {
       print path + ' does not exist!';
       return;
   }
   def server = buildServer(path)
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
   
   // Get current annotations, rescale
   def oldObjects = getAnnotationObjects().findAll{it.getPathClass() == getPathClass(currClassName)}
   def transform = java.awt.geom.AffineTransform.getScaleInstance(downsample, downsample)
   def newObjects = oldObjects.collect {p -> PathObjectTools.transformObject(p, transform, false)}
   
   // Delete old annotations
   clearSelectedObjects(false);
   
   // add resulting annotation object (and update current threshold)
   addObjects(newObjects)
   current_thresh++;
      
   print "Done!"
    
}

Thread.sleep(100);
javafx.application.Platform.runLater {
getCurrentViewer().getImageRegionStore().cache.clear();
System.gc();
}
Thread.sleep(100);
