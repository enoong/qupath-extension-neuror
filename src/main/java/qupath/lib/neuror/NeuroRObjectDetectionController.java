/**
 * Sample Skeleton for 'NeuroR_Segmentation.fxml' Controller Class
 */

package qupath.lib.neuror;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import qupath.lib.gui.QuPathGUI;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class NeuroRObjectDetectionController implements Initializable {

    private QuPathGUI qupath;

    public NeuroRObjectDetectionController(QuPathGUI qupath) {
        this.qupath = qupath;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anacondaEnvPath"
    private Button anacondaEnvPath; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField1"
    private TextField folderTextField1; // Value injected by FXMLLoader

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the default text for TextField1
        String defaultAnacondaEnvPath = NeuroRExtension.anacondaEnvPathProperty().get();
        folderTextField1.setText(defaultAnacondaEnvPath);
        // Set the default text for TextField2
        String defaultPythonExecPath = NeuroRExtension.pythonExecPathProperty().get();
        folderTextField2.setText(defaultPythonExecPath);
        // Set the default text for TextField3
        String defaultNeuroRObjectDetectionExecPath = NeuroRExtension.neurorObjectDetectionExecPathProperty().get();
        folderTextField3.setText(defaultNeuroRObjectDetectionExecPath);

        // Add items to the choiceBox1 (img_lib)
        choiceBox1.getItems().addAll("openslide", "bioformats", "dicom");
        // Set a default value
        choiceBox1.setValue("openslide");

        // Add items to the choiceBox2 (level)
        choiceBox2.getItems().addAll("0", "1", "2", "3");
        // Set a default value
        choiceBox2.setValue("1");

        // Add items to the ChoiceBox3 (num_gpus)
        choiceBox3.getItems().addAll("1", "2");
        // Set a default value
        choiceBox3.setValue("2");

        // Set a default value (batch_size)
        textField4.setText("128");

        // Set a default value (className)
        textField3.setText("Tumor");
    }

    @FXML // fx:id="execute_file"
    private Button execute_file; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField2"
    private TextField folderTextField2; // Value injected by FXMLLoader

    @FXML // fx:id="image_path"
    private Button image_path; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField3"
    private TextField folderTextField3; // Value injected by FXMLLoader

    @FXML // fx:id="model_path"
    private Button model_path; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField4"
    private TextField folderTextField4; // Value injected by FXMLLoader

    @FXML // fx:id="output_path"
    private Button output_path; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField5"
    private TextField folderTextField5; // Value injected by FXMLLoader

    @FXML // fx:id="pythonExecPath"
    private Button pythonExecPath; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField6"
    private TextField folderTextField6; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField7"
    private TextField folderTextField7; // Value injected by FXMLLoader

    @FXML // fx:id="textField1" (patch_size)
    private TextField textField1; // Value injected by FXMLLoader

    @FXML // fx:id="textField3" (classNames)
    private TextField textField3; // Value injected by FXMLLoader

    @FXML // fx:id="textField4" (batch_size)
    private TextField textField4; // Value injected by FXMLLoader

    @FXML // fx:id="textField5" (selectedClassNames)
    private TextField textField5; // Value injected by FXMLLoader

    @FXML // fx:id="choiceBox1" (img_lib)
    private ChoiceBox choiceBox1;

    @FXML // fx:id="choiceBox2" (level)
    private ChoiceBox choiceBox2;

    @FXML // fx:id="choiceBox3" (num_gpus)
    private ChoiceBox choiceBox3;


    @FXML
    private void handleButtonClick1(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folders");

        Stage stage = (Stage) folderTextField1.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath().replace('\\','/');
            NeuroRExtension.anacondaEnvPathProperty.setValue(folderPath);
            folderTextField1.setText(folderPath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick2(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Stage stage = (Stage) folderTextField2.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\','/');
            NeuroRExtension.pythonExecPathProperty.setValue(filePath);
            folderTextField2.setText(filePath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick3(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Stage stage = (Stage) folderTextField3.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\','/');
            NeuroRExtension.neurorSegmentationExecPath.setValue(filePath);
            folderTextField3.setText(filePath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick4(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Stage stage = (Stage) folderTextField4.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\','/');
            folderTextField4.setText(filePath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick5(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folders");

        Stage stage = (Stage) folderTextField5.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath().replace('\\','/');

            // Check if the last character is not a file separator ("/" for Unix or "\" for Windows), then append it.
            if (!folderPath.endsWith(File.separator)) {
                folderPath = folderPath + File.separator;
            }

            folderTextField5.setText(folderPath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick6(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folders");

        Stage stage = (Stage) folderTextField6.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath().replace('\\','/');

            // Check if the last character is not a file separator ("/" for Unix or "\" for Windows), then append it.
            if (!folderPath.endsWith(File.separator)) {
                folderPath = folderPath + File.separator;
            }

            folderTextField6.setText(folderPath);
            saveToGroovyScript();
        }
    }

    @FXML
    private void handleButtonClick7(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folders");

        Stage stage = (Stage) folderTextField7.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath().replace('\\','/');

            // Check if the last character is not a file separator ("/" for Unix or "\" for Windows), then append it.
            if (!folderPath.endsWith(File.separator)) {
                folderPath = folderPath + File.separator;
            }

            folderTextField7.setText(folderPath);
            saveToGroovyScript();
        }
    }

    @FXML // fx:id="Run"
    private Button Run; // Value injected by FXMLLoader

    //define run button action
    String scriptPath = null;
    @FXML
    private void handleRunButtonClick(ActionEvent event) {
        saveToGroovyScript();
        File file = new File(scriptPath);
        qupath.getScriptEditor().showScript(file);
    }

    private void saveToGroovyScript() {
        try {
            String scriptTemplate =
                "import org.locationtech.jts.io.WKTWriter\n" +
                "import qupath.lib.io.GsonTools\n" +
                "\n" +
                "// --- SET THESE PARAMETERS -----------------------------------------------------------\n" +
                "\n" +
                "// Parameters for running Python and Anaconda\n" +
                "def anacondaEnvPath = '%s'\n" +
                "def pythonExecPath = '%s'\n" +
                "def execute_file = '%s'\n" +
                "\n" +
                "// Please enter the absolute paths for the model, the original image, and the output directory for the TIFF files\n" +
                "//def model_path = \"E:/Neuro_Qupath_Det/model/Mitosis_test_ds1_512.net\"\n" +
                "def model_path = '%s'\n" +
                "def image_path = '%s'\n" +
                "def output_path = '%s'\n" +
                "\n" +
                "// Input name of your annotation (geojson) folder\n" +
                "json_export_folder = '%s'\n" +
                "\n" +
                "def masksPath = output_path // path to where TIFFs are stored \n" +
                "\n" +
                "def imageData = getCurrentImageData();\n" +
                "image_name = imageData.getServer().getMetadata().getName()\n" +
                "\n" +
                "// Image processing library\n" +
                "def img_lib = \"%s\" // or \"bioformats\" or \"dicom\"\n" +
                "\n" +
                "// If importing DICOM, set to true\n" +
                "\n" +
                "if (img_lib == \"dicom\")\n" +
                "{\n" +
                "    image_name = image_name.split('-')[0].trim()\n" +
                "\n" +
                "}\n" +
                "\n" +
                "int patch_size = %s \n" +
                "int level = %s                           // which level to extract segmentation from (choosing 0 may be slow)\n" +
                "def extension = \".tiff\"                 // pyramidal TIFF\n" +
                "//def classNames = [\"Epidermis\", \"Adnexa\"]    // names of classes of interest (in this case two classes, excluding the background class)\n" +
                "classNames = [%s] \n" +
                "def channel = 0 // 0-based index for the channel to threshold\n" +
                "int batch_size = %s // The unit of the number of images during inference ex) 4, 16, 32, 64..\n" +
                "def dev_mode = \"gpu\" // or \"cpu\"\n" +
                "def if_config = \"false\" // or \"true\" select between 32-bit and 16-bit floating points\n" +
                "int num_gpus = %s \n" +
                "\n" +
                "def selectedClassNames = [%s]\n" +
                "\n" +
                "// ------------------------------------------------------------------------------------\n" +
                "\n" +
                "// --- Export Json Annotations --------------------------------------------------------\n" +
                "\n" +
                "// export_focus_geojson .. etc\n" +
                "export_extension= \".geojson\"\n" +
                "// .geojson\n" +
                "\n" +
                "// Get annotations\n" +
                "//def annotations = getAnnotationObjects().collect {new qupath.lib.objects.PathAnnotationObject(it.getROI(), it.getPathClass())}\n" +
                "def annotations = getAnnotationObjects().findAll { annotation ->\n" +
                "    selectedClassNames.any { className -> annotation.getPathClass().getName() == className } && annotation.getROI()\n" +
                "}\n" +
                "// Get Gson instance\n" +
                "def gson = GsonTools.getInstance(true)\n" +
                "\n" +
                "// Create 'annotations' directory if doesn't exist\n" +
                "def path = buildFilePath(json_export_folder)\n" +
                "\n" +
                "\n" +
                "def filename = GeneralTools.getNameWithoutExtension(getProjectEntry().getImageName())\n" +
                "\n" +
                "if (img_lib == \"dicom\")\n" +
                "{  \n" +
                "    filename = filename.split('-')[0].trim()\n" +
                "    filename = filename.substring(0, filename.lastIndexOf('.'))\n" +
                "}\n" +
                "\n" +
                "new File(path).mkdir()\n" +
                "\n" +
                "// Write to a new file inside the 'annotations' directory\n" +
                "File file = new File(path + \"/\" + filename + export_extension)\n" +
                "file.write(gson.toJson(annotations))\n" +
                "\n" +
                "def json_path = path + \"/\" + filename + export_extension\n" +
                "\n" +
                "print 'exported!'\n" +
                "\n" +
                "// --- RUN PYTHON MODULE --------------------------------------------------------------\n" +
                "\n" +
                "def command = [pythonExecPath, execute_file, model_path, json_path, image_path, image_name, output_path, patch_size as String, level as String, classNames as String, batch_size as String, dev_mode, if_config as String, img_lib, num_gpus as String]\n" +
                "def processBuilder = new ProcessBuilder(command)\n" +
                "processBuilder.directory(new File(\".\"))\n" +
                "processBuilder.environment().put(\"PATH\", anacondaEnvPath + \";\" + System.env.PATH)\n" +
                "processBuilder.redirectErrorStream(true)\n" +
                "def process = processBuilder.start()\n" +
                "def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))\n" +
                "reader.eachLine {\n" +
                "    println it\n" +
                "}\n" +
                "\n" +
                "process.waitFor()\n" +
                "print \"Inference Done\"\n" +
                "\n" +
                "// ------------------------------------------------------------------------------------\n" +
                "\n" +
                "// --- Import Json Annotation ---------------------------------------------------------\n" +
                "\n" +
                "// Input name of your annotation (geojson) folder\n" +
                "json_folder = output_path\n" +
                "//export_focus .... etc\n" +
                "json_extension = '.geojson'\n" +
                "\n" +
                "// Instantiate tools\n" +
                "//def gson = GsonTools.getInstance(true);\n" +
                "\n" +
                "// Get path of image\n" +
                "def import_filename = GeneralTools.getNameWithoutExtension(getProjectEntry().getImageName())\n" +
                "\n" +
                "if (img_lib == \"dicom\")\n" +
                "{  \n" +
                "    import_filename = import_filename.split('-')[0].trim()\n" +
                "    import_filename = import_filename.substring(0, import_filename.lastIndexOf('.'))\n" +
                "}\n" +
                "// Prepare template\n" +
                "def type = new com.google.gson.reflect.TypeToken<List<qupath.lib.objects.PathObject>>() {}.getType();\n" +
                "def json = new File(buildFilePath(json_folder, import_filename + \"_predict\" + json_extension))\n" +
                "\n" +
                "// Deserialize\n" +
                "deserializedAnnotations = gson.fromJson(json.getText('UTF-8'), type);\n" +
                "\n" +
                "// Add to image\n" +
                "addObjects(deserializedAnnotations);\n" +
                "\n" +
                "// Resolve hierarchy\n" +
                "resolveHierarchy()\n" +
                "\n" +
                "def IMname = getProjectEntry().getImageName()\n" +
                "print 'Done: ' + IMname\n";

            String classNamesText = textField3.getText();
            List<String> classes = Arrays.asList(classNamesText.split("\\s*,\\s*"));
            StringBuilder classNames = new StringBuilder();
            for (String i : classes) {
                if (classNames.length() != 0) {
                    classNames.append(",");
                }
                classNames.append("\"" + i + "\"");
            }

            String selectedClassNamesText = textField5.getText();
            List<String> selectedClasses = Arrays.asList(selectedClassNamesText.split("\\s*,\\s*"));
            StringBuilder selectedClassNames = new StringBuilder();
            for (String i : selectedClasses) {
                if (selectedClassNames.length() != 0) {
                    selectedClassNames.append(",");
                }
                selectedClassNames.append("\"" + i + "\"");
            }

            String filledScript = String.format(
                    scriptTemplate,
                    folderTextField1.getText(),
                    folderTextField2.getText().replace('\\','/'),
                    folderTextField3.getText().replace('\\','/'),
                    folderTextField4.getText().replace('\\','/'),
                    folderTextField5.getText().replace('\\','/'),
                    folderTextField6.getText().replace('\\','/'),
                    folderTextField7.getText().replace('\\','/'),
                    choiceBox1.getValue(), //img_lib
                    textField1.getText(), //patch_size
                    choiceBox2.getValue(), //level
                    classNames.toString(), //className
                    textField4.getText(), //batchSize
                    choiceBox3.getValue(), //num_gpus
                    selectedClassNames.toString() //selectedClassNames
            );

            scriptPath = folderTextField6.getText() + "run_segmentation.groovy"; // Replace with your actual script path
            try (PrintWriter writer = new PrintWriter(scriptPath, "UTF-8")) {
                writer.print(filledScript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert Run != null : "fx:id=\"Run\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert anacondaEnvPath != null : "fx:id=\"anacondaEnvPath\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert choiceBox1 != null : "fx:id=\"choiceBox1\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert choiceBox2 != null : "fx:id=\"choiceBox2\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert choiceBox3 != null : "fx:id=\"choiceBox3\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert execute_file != null : "fx:id=\"execute_file\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField1 != null : "fx:id=\"folderTextField1\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField2 != null : "fx:id=\"folderTextField2\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField3 != null : "fx:id=\"folderTextField3\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField4 != null : "fx:id=\"folderTextField4\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField5 != null : "fx:id=\"folderTextField5\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert folderTextField6 != null : "fx:id=\"folderTextField6\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert image_path != null : "fx:id=\"image_path\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert model_path != null : "fx:id=\"model_path\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert output_path != null : "fx:id=\"output_path\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert pythonExecPath != null : "fx:id=\"pythonExecPath\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert textField1 != null : "fx:id=\"textField1\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert textField2 != null : "fx:id=\"textField2\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert textField3 != null : "fx:id=\"textField3\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";
        assert textField4 != null : "fx:id=\"textField4\" was not injected: check your FXML file 'NeuroR_Segmentation.fxml'.";

    }
     */
}