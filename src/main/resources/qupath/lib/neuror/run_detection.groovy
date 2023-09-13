import org.locationtech.jts.io.WKTWriter
import qupath.lib.io.GsonTools

// --- SET THESE PARAMETERS -----------------------------------------------------------

// Parameters for running Python and Anaconda
def anacondaEnvPath = "%s"
def pythonExecPath = "%s"
def execute_file = "%s"

// Please enter the absolute paths for the model, the original image, and the output directory for the TIFF files
//def model_path = "E:/Neuro_Qupath_Det/model/Mitosis_test_ds1_512.net"
def model_path = "%s"
def image_path = "%s"
def output_path = "%s"

// Input name of your annotation (geojson) folder
json_export_folder = "%s"

def masksPath = output_path // path to where TIFFs are stored 

def imageData = getCurrentImageData();
image_name = imageData.getServer().getMetadata().getName()

// Image processing library
def img_lib = "%s" // or "bioformats" or "dicom"

// If importing DICOM, set to true

if (img_lib == "dicom")
{
    image_name = image_name.split('-')[0].trim()

}

int patch_size = %s
int level = %s                           // which level to extract segmentation from (choosing 0 may be slow)
def extension = ".tiff"                 // pyramidal TIFF
//def classNames = ["Epidermis", "Adnexa"]    // names of classes of interest (in this case two classes, excluding the background class)
classNames = [%s]
def channel = 0// 0-based index for the channel to threshold
int batch_size = %s // The unit of the number of images during inference ex) 4, 16, 32, 64..
def dev_mode = "gpu" // or "cpu"
def if_config = "false" // or "true" select between 32-bit and 16-bit floating points
int num_gpus = %s

def selectedClassNames = [%s]

// ------------------------------------------------------------------------------------

// --- Export Json Annotations --------------------------------------------------------

// export_focus_geojson .. etc
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

def command = [pythonExecPath, execute_file, model_path, json_path, image_path, image_name, output_path, patch_size as String, level as String, classNames as String, batch_size as String, dev_mode, if_config as String, img_lib, num_gpus as String]
def processBuilder = new ProcessBuilder(command)
processBuilder.directory(new File("."))
processBuilder.environment().put("PATH", anacondaEnvPath + ";" + System.env.PATH)
processBuilder.redirectErrorStream(true)
def process = processBuilder.start()
def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
reader.eachLine {
    println it
}

process.waitFor()
print "Inference Done"

// ------------------------------------------------------------------------------------

// --- Import Json Annotation ---------------------------------------------------------

// Input name of your annotation (geojson) folder
json_folder = output_path
//export_focus .... etc
json_extension = '.geojson'

// Instantiate tools
//def gson = GsonTools.getInstance(true);

// Get path of image
def import_filename = GeneralTools.getNameWithoutExtension(getProjectEntry().getImageName())

if (img_lib == "dicom")
{  
    import_filename = import_filename.split('-')[0].trim()
    import_filename = import_filename.substring(0, import_filename.lastIndexOf('.'))
}
// Prepare template
def type = new com.google.gson.reflect.TypeToken<List<qupath.lib.objects.PathObject>>() {}.getType();
def json = new File(buildFilePath(json_folder, import_filename + "_predict" + json_extension))

// Deserialize
deserializedAnnotations = gson.fromJson(json.getText('UTF-8'), type);

// Add to image
addObjects(deserializedAnnotations);

// Resolve hierarchy
resolveHierarchy()

print "Done!"
