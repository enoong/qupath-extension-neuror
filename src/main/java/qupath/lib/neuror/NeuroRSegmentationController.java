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
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
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

import static qupath.lib.scripting.QP.*;


public class NeuroRSegmentationController implements Initializable {

    private QuPathGUI qupath;

    public NeuroRSegmentationController(QuPathGUI qupath) {
        this.qupath = qupath;
    }


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML //environment path titledpane
    private TitledPane envPathPane;

    @FXML // fx:id="anacondaEnvPath"
    private Button anacondaEnvPath; // Value injected by FXMLLoader

    @FXML // fx:id="folderTextField1"
    private TextField folderTextField1; // Value injected by FXMLLoader

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

    @FXML // fx:id="textField1" (patch_size)
    private TextField textField1; // Value injected by FXMLLoader

    @FXML // fx:id="textField2" (overlap)
    private TextField textField2; // Value injected by FXMLLoader

    @FXML // fx:id="textField3" (classNames)
    private TextField textField3; // Value injected by FXMLLoader

    @FXML // fx:id="textField4" (batch_size)
    private TextField textField4; // Value injected by FXMLLoader

    @FXML // fx:id="textField5" (groovy_script_name)
    private TextField textField5; // Value injected by FXMLLoader

    @FXML // fx:id="choiceBox1" (img_lib)
    private ChoiceBox<String> choiceBox1;

    @FXML // fx:id="choiceBox2" (level)
    private ChoiceBox<String> choiceBox2;

    @FXML // fx:id="choiceBox3" (num_gpus)
    private ChoiceBox<String> choiceBox3;

    @FXML // fx:id="Run"
    private Button Run; // Value injected by FXMLLoader

    @FXML // fx:id="Run1
    private Button Run1; // Value injected by FXMLLoader

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the default text for TextField1
        String defaultAnacondaEnvPath = NeuroRExtension.anacondaEnvPathProperty().get();
        folderTextField1.setText(defaultAnacondaEnvPath);
        // Set the default text for TextField2
        String defaultPythonExecPath = NeuroRExtension.pythonExecPathProperty().get();
        folderTextField2.setText(defaultPythonExecPath);
        // Set the default text for TextField3
        String defaultNeuroRExecPath = NeuroRExtension.neurorSegmentationExecPathProperty().get();
        folderTextField3.setText(defaultNeuroRExecPath);

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

        // Set a default value (overlap)
        textField2.setText("0");

        // Set a default value (batch_size)
        textField4.setText("128");

        // Set a default value (className)
        textField3.setText("Tumor");
    }

    @FXML
    private void envPathPaneClicked(MouseEvent event) {
        envPathPane.getScene().getWindow().sizeToScene();
    }

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
        }
    }

    @FXML //model_path
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
            updateScriptName();
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
        }
    }

    //define run button action
    @FXML
    private void handleRunButtonClick(ActionEvent event) {
        File file = new File(saveToGroovyScript());
        qupath.getScriptEditor().showScript(file);
    }

    //define generateScriptName button action
    @FXML
    private void generateScriptName(ActionEvent event) {
        updateScriptName();
    }

    private String saveToGroovyScript() {
        try {
            String segmentation_script = new String(NeuroRSegmentationController.class
                    .getResourceAsStream("/qupath/lib/neuror/run_segmentation.groovy").readAllBytes());

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

            //fill run_segmentation.groovy
            String filled_segmentation_script = String.format(
                    segmentation_script,
                    folderTextField1.getText(),
                    folderTextField2.getText().replace('\\','/'),
                    folderTextField3.getText().replace('\\','/'),
                    folderTextField4.getText().replace('\\','/'),
                    folderTextField5.getText().replace('\\','/'),
                    folderTextField6.getText().replace('\\','/'),
                    choiceBox1.getValue(), //img_lib
                    textField1.getText(), //patch_size
                    choiceBox2.getValue(), //level
                    classNames.toString(), //className
                    textField2.getText(), //overlap
                    textField4.getText(), //batchSize
                    choiceBox3.getValue() //num_gpus
            );

            String scriptName = textField5.getText();

            if (scriptName == "") {
                updateScriptName();
                scriptName = textField5.getText();
            }
            String scriptPath = buildFilePath(PROJECT_BASE_DIR, "scripts", scriptName);
            makePathInProject("scripts");
            try (PrintWriter writer = new PrintWriter(scriptPath, "UTF-8")) {
                writer.print(filled_segmentation_script);
            }
            return(scriptPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void updateScriptName() {
        //calculate downsample
        int downsample = 1;
        int level = Integer.parseInt(choiceBox2.getValue());
        downsample = (int) Math.pow(4, level);

        //get script name
        String scriptName = "downsample_" + String.valueOf(downsample) +
                "_patch_size_" + textField1.getText() +
                "_overlap_" + textField2.getText() +
                "_segmentation.groovy";

        //set textField5 to scriptname
        textField5.setText(scriptName);
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
