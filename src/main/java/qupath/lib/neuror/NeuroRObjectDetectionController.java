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
        choiceBox3.setValue("1");

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
            String[] modelParseArray = filePath.split("_");
            for (String str : modelParseArray) {
                if (str.contains("ds")) {
                    int ds_level = (int)(Math.log(Double.parseDouble(str.substring(2))) / Math.log(4));
                    choiceBox2.setValue(Integer.toString(ds_level));
                }
            }
            for (String str : modelParseArray) {
                try {
                    int patchSize = Integer.parseInt(str);
                    if (patchSize % 64 == 0) {
                        textField1.setText(Integer.toString(patchSize));
                        break;
                    }
                }
                catch (Exception e){
                    continue;
                }
            }
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

            String detection_script = new String(NeuroRObjectDetectionController.class
                    .getResourceAsStream("/qupath/lib/neuror/run_detection.groovy").readAllBytes());

            //Edit class names into appropriate string
            //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
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

            String filled_detection_script = String.format(
                    detection_script,
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
                writer.print(filled_detection_script);
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