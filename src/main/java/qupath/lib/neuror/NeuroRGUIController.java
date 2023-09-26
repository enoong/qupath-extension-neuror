package qupath.lib.neuror;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import static qupath.lib.scripting.QP.getCurrentServer;

public class NeuroRGUIController implements Initializable {

    private QuPathGUI qupath;
    public NeuroRGUIController(QuPathGUI qupath) {
        this.qupath = qupath;
    }

    @FXML
    private TabPane tabPane;

    @FXML //segmentation fields
    private TextField segModelPath;
    @FXML
    private TextField segImagePath;
    @FXML
    private TextField segOutputPath;
    @FXML
    private ChoiceBox<String> segImgLibChoiceBox;
    @FXML
    private TextField segPatchSize;
    @FXML
    private ChoiceBox<String> segLevelChoiceBox;
    @FXML
    private TextField segOverlap;
    @FXML
    private TextField segBatchSize;
    @FXML
    private TextField segClassNames;
    @FXML
    private ChoiceBox<String> segNumGPUChoiceBox;
    @FXML
    private CheckBox segROICheckBox;
    @FXML
    private TextField segJsonExportPath;
    @FXML
    private Button segJsonExportPathButton;
    @FXML
    private TextField segROIClassNames;
    @FXML
    private TextField segScriptName;

    @FXML //object detection fields
    private TextField detModelPath;
    @FXML
    private TextField detImagePath;
    @FXML
    private TextField detOutputPath;
    @FXML
    private TextField detJsonExportPath;
    @FXML
    private ChoiceBox<String> detImgLibChoiceBox;
    @FXML
    private TextField detPatchSize;
    @FXML
    private ChoiceBox<String> detLevelChoiceBox;
    @FXML
    private TextField detBatchSize;
    @FXML
    private TextField detClassNames;
    @FXML
    private TextField detSelectedClassNames;
    @FXML
    private ChoiceBox<String> detNumGPUChoiceBox;
    @FXML
    private TextField detROIClassNames;
    @FXML
    private TextField detScriptName;

    @FXML //Environment Path Fields
    private TextField anacondaEnvPath;
    @FXML
    private TextField pythonExecPath;
    @FXML
    private TextField segExecPath;
    @FXML
    private TextField detExecPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the default text for anacondaEnvPath
        String defaultAnacondaEnvPath = NeuroRExtension.anacondaEnvPathProperty().get();
        anacondaEnvPath.setText(defaultAnacondaEnvPath);
        // Set the default text for pythonExecPath
        String defaultPythonExecPath = NeuroRExtension.pythonExecPathProperty().get();
        pythonExecPath.setText(defaultPythonExecPath);
        // Set the default text for segExecPath
        String defaultSegExecPath = NeuroRExtension.neurorSegmentationExecPathProperty().get();
        segExecPath.setText(defaultSegExecPath);
        // Set the default text for detExecPath
        String defaultDetExecPath = NeuroRExtension.neurorObjectDetectionExecPathProperty().get();
        detExecPath.setText(defaultDetExecPath);

        // Add items to img_lib
        segImgLibChoiceBox.getItems().addAll("openslide", "bioformats", "dicom");
        detImgLibChoiceBox.getItems().addAll("openslide", "bioformats", "dicom");
        // Set a default value
        segImgLibChoiceBox.setValue("openslide");
        detImgLibChoiceBox.setValue("openslide");

        // Add items to level
        segLevelChoiceBox.getItems().addAll("0", "1", "2", "3");
        detLevelChoiceBox.getItems().addAll("0", "1", "2", "3");
        // Set a default value
        segLevelChoiceBox.setValue("1");
        detLevelChoiceBox.setValue("1");

        // Add items to num_gpus
        segNumGPUChoiceBox.getItems().addAll("1", "2");
        detNumGPUChoiceBox.getItems().addAll("1", "2");
        // Set a default value
        segNumGPUChoiceBox.setValue("2");
        detNumGPUChoiceBox.setValue("2");

        // Set a default value (overlap)
        segOverlap.setText("0");

        // Set a default value (batch_size)
        segBatchSize.setText("128");
        detBatchSize.setText("128");

        // Set a default value (className)
        segClassNames.setText("Tumor");
        detClassNames.setText("Tumor");

        // Tried to get dialog to resize when changing tabs
        // -- doesn't seem to work, apparently..
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                        System.out.println("test");
                        tabPane.getScene().getWindow().sizeToScene();
                    }
                }
        );
    }

    //**************************************
    // directory, file chooser methods
    //**************************************
    private String getDirectory(Stage stage, String setInitialDirectory) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        if (setInitialDirectory != null) {
            directoryChooser.setInitialDirectory(new File(buildFilePath(setInitialDirectory)));
        }
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath().replace('\\', '/');
            return folderPath;
        }
        else {
            return null;
        }
    }

    private String getFile(Stage stage, String setInitialDirectory) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        if (setInitialDirectory != null) {
            fileChooser.setInitialDirectory(new File(buildFilePath(setInitialDirectory)));
        }
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\', '/');
            return filePath;
        }
        else {
            return null;
        }
    }


    //************************************
    // Environment Paths button handlers
    //************************************
    @FXML
    private void anacondaEnvPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) anacondaEnvPath.getScene().getWindow();
        String folderPath = getDirectory(stage, null);

        NeuroRExtension.anacondaEnvPathProperty.setValue(folderPath);
        anacondaEnvPath.setText(folderPath);
    }

    @FXML
    private void pythonExecPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) pythonExecPath.getScene().getWindow();
        String filePath = getFile(stage, null);

        NeuroRExtension.pythonExecPathProperty.setValue(filePath);
        pythonExecPath.setText(filePath);
    }

    @FXML
    private void segExecPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) segExecPath.getScene().getWindow();
        String filePath = getFile(stage, null);

        NeuroRExtension.neurorSegmentationExecPath.setValue(filePath);
        segExecPath.setText(filePath);
    }

    @FXML
    private void detExecPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) detExecPath.getScene().getWindow();
        String filePath = getFile(stage, null);

        NeuroRExtension.neurorObjectDetectionExecPath.setValue(filePath);
        detExecPath.setText(filePath);
    }

    //************************************
    // Segmentation button handlers
    //************************************

    @FXML //model_path  --  may change to use getFile() later
    private void segModelPathButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setInitialDirectory(new File(buildFilePath(PROJECT_BASE_DIR)));

        Stage stage = (Stage) segModelPath.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\','/');
            segModelPath.setText(filePath);
            String[] modelParseArray = filePath.split("_");
            for (String str : modelParseArray) {
                if (str.contains("ds")) {
                    int ds_level = (int)(Math.log(Double.parseDouble(str.substring(2))) / Math.log(4));
                    segLevelChoiceBox.setValue(Integer.toString(ds_level));
                }
            }
            for (String str : modelParseArray) {
                try {
                    int patchSize = Integer.parseInt(str);
                    if (patchSize % 64 == 0) {
                        segPatchSize.setText(Integer.toString(patchSize));
                        break;
                    }
                }
                catch (Exception e){
                    continue;
                }
            }
            updateSegmentationScriptName();
        }
    }
    @FXML
    private void segImagePathButtonClick(ActionEvent event) {
        Stage stage = (Stage) segImagePath.getScene().getWindow();

        String folderPath = null;

        if (getCurrentServer() == null) {
            //String.format() doesn't work if argument is null
            String folderResult = getDirectory(stage, null);
            if (folderResult != null) {
                folderPath = String.format(folderResult);
            }
        }

        else {
            StringBuilder imagePath = new StringBuilder();
            String[] pathArray = getCurrentServer().getPath().split("/");
            for (int i = 1; i < pathArray.length - 1; i++) {
                imagePath.append(pathArray[i] + "/");
            }
            String initialDirectory = imagePath.toString();
            //String.format() doesn't work if argument is null
            String folderResult = getDirectory(stage, initialDirectory);
            if (folderResult != null) {
                folderPath = String.format(folderResult);
            }
        }

        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        segImagePath.setText(folderPath);
    }

    @FXML
    private void segOutputPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) segOutputPath.getScene().getWindow();
        String initialDirectory = PROJECT_BASE_DIR;

        String folderPath = getDirectory(stage, initialDirectory);
        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        segOutputPath.setText(folderPath);
    }

    @FXML
    private void segROICheckBoxClicked(ActionEvent event) {
        if (segROICheckBox.isSelected()) {
            segJsonExportPath.setDisable(false);
            segJsonExportPathButton.setDisable(false);
            segROIClassNames.setDisable(false);
        }
        else {
            segJsonExportPath.setDisable(true);
            segJsonExportPathButton.setDisable(true);
            segROIClassNames.setDisable(true);
        }
    }

    //select json export folder
    @FXML
    private void segJsonExportPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) segJsonExportPath.getScene().getWindow();
        String folderPath = getDirectory(stage, PROJECT_BASE_DIR);

        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        segJsonExportPath.setText(folderPath);
    }

    //define run button action
    @FXML
    private void segCreateScriptButtonClick(ActionEvent event) {
        File file = new File(saveToSegmentationGroovyScript());
        qupath.getScriptEditor().showScript(file);
    }

    //define generateScriptName button action
    @FXML
    private void segGenerateScriptNameButtonClick(ActionEvent event) {
        updateSegmentationScriptName();
    }

    private void updateSegmentationScriptName() {
        //calculate downsample
        int downsample = 1;
        int level = Integer.parseInt(segLevelChoiceBox.getValue());
        downsample = (int) Math.pow(4, level);

        //get script name
        String scriptName = "ds_" + String.valueOf(downsample) +
                "_ps_" + segPatchSize.getText() +
                "_ov_" + segOverlap.getText();

        if (segROICheckBox.isSelected() == false) {
            scriptName += "_segmentation.groovy";
        }
        else {
            scriptName += "_segmentation_roi.groovy";
        }

        //set scriptname
        segScriptName.setText(scriptName);
    }

    private String saveToSegmentationGroovyScript() {
        try {
            String filled_segmentation_script;

            //Edit class names into appropriate string
            //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
            String classNamesText = segClassNames.getText();
            List<String> classes = Arrays.asList(classNamesText.split("\\s*,\\s*"));
            StringBuilder classNames = new StringBuilder();
            for (String i : classes) {
                if (classNames.length() != 0) {
                    classNames.append(",");
                }
                classNames.append("\"" + i + "\"");
            }

            // run for whole image
            if (segROICheckBox.isSelected() == false) {
                String segmentation_script = new String(NeuroRSegmentationController.class
                        .getResourceAsStream("/qupath/lib/neuror/run_segmentation_roi.groovy").readAllBytes());


                //fill run_segmentation_roi.groovy
                filled_segmentation_script = String.format(
                        segmentation_script,
                        anacondaEnvPath.getText(),
                        pythonExecPath.getText().replace('\\', '/'),
                        segExecPath.getText().replace('\\', '/'),
                        segModelPath.getText().replace('\\', '/'),
                        segImagePath.getText().replace('\\', '/'),
                        segOutputPath.getText().replace('\\', '/'),
                        "", //blank space for json export folder
                        segImgLibChoiceBox.getValue(), //img_lib
                        segPatchSize.getText(), //patch_size
                        segLevelChoiceBox.getValue(), //level
                        classNames.toString(), //className
                        segOverlap.getText(), //overlap
                        segBatchSize.getText(), //batchSize
                        segNumGPUChoiceBox.getValue(), //num_gpus
                        "" //blank space for ROI class names
                );
            }
            //ROI class names specified
            else {
                String segmentation_script = new String(NeuroRSegmentationController.class
                        .getResourceAsStream("/qupath/lib/neuror/run_segmentation_roi.groovy").readAllBytes());

                String ROIClassNamesText = segROIClassNames.getText();
                List<String> ROIClasses = Arrays.asList(ROIClassNamesText.split("\\s*,\\s*"));
                StringBuilder ROIClassNames = new StringBuilder();
                for (String i : ROIClasses) {
                    if (ROIClassNames.length() != 0) {
                        ROIClassNames.append(",");
                    }
                    ROIClassNames.append("\"" + i + "\"");
                }

                //fill run_segmentation_roi.groovy
                filled_segmentation_script = String.format(
                        segmentation_script,
                        anacondaEnvPath.getText(),
                        pythonExecPath.getText().replace('\\', '/'),
                        segExecPath.getText().replace('\\', '/'),
                        segModelPath.getText().replace('\\', '/'),
                        segImagePath.getText().replace('\\', '/'),
                        segOutputPath.getText().replace('\\', '/'),
                        segJsonExportPath.getText().replace('\\','/'),
                        segImgLibChoiceBox.getValue(), //img_lib
                        segPatchSize.getText(), //patch_size
                        segLevelChoiceBox.getValue(), //level
                        classNames.toString(), //className
                        segOverlap.getText(), //overlap
                        segBatchSize.getText(), //batchSize
                        segNumGPUChoiceBox.getValue(), //num_gpus
                        ROIClassNames.toString() //ROI Class names
                );
            }

            String scriptName = segScriptName.getText();

            if (scriptName == "") {
                updateSegmentationScriptName();
                scriptName = segScriptName.getText();
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

    //***************************************
    //Object Detection Button Handlers
    //***************************************

    @FXML
    private void detModelPathButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Stage stage = (Stage) detModelPath.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath().replace('\\','/');
            detModelPath.setText(filePath);
            String[] modelParseArray = filePath.split("_");
            for (String str : modelParseArray) {
                if (str.contains("ds")) {
                    int ds_level = (int)(Math.log(Double.parseDouble(str.substring(2))) / Math.log(4));
                    detImgLibChoiceBox.setValue(Integer.toString(ds_level));
                }
            }
            for (String str : modelParseArray) {
                try {
                    int patchSize = Integer.parseInt(str);
                    if (patchSize % 64 == 0) {
                        detPatchSize.setText(Integer.toString(patchSize));
                        break;
                    }
                }
                catch (Exception e){
                    continue;
                }
            }
            updateDetectionScriptName();
        }
    }

    @FXML
    private void detImagePathButtonClick(ActionEvent event) {
        Stage stage = (Stage) detImagePath.getScene().getWindow();

        String folderPath = null;

        if (getCurrentServer() == null) {
            //String.format() doesn't work if argument is null
            String folderResult = getDirectory(stage, null);
            if (folderResult != null) {
                folderPath = String.format(folderResult);
            }
        }

        else {
            StringBuilder imagePath = new StringBuilder();
            String[] pathArray = getCurrentServer().getPath().split("/");
            for (int i = 1; i < pathArray.length - 1; i++) {
                imagePath.append(pathArray[i] + "/");
            }
            String initialDirectory = imagePath.toString();
            //String.format() doesn't work if argument is null
            String folderResult = getDirectory(stage, initialDirectory);
            if (folderResult != null) {
                folderPath = String.format(folderResult);
            }
        }

        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        detImagePath.setText(folderPath);
    }

    @FXML
    private void detOutputPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) detOutputPath.getScene().getWindow();
        String initialDirectory = PROJECT_BASE_DIR;

        String folderPath = getDirectory(stage, initialDirectory);
        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        detOutputPath.setText(folderPath);
    }

    @FXML
    private void detJsonExportPathButtonClick(ActionEvent event) {
        Stage stage = (Stage) detJsonExportPath.getScene().getWindow();
        String folderPath = getDirectory(stage, PROJECT_BASE_DIR);

        if (folderPath != null && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        detJsonExportPath.setText(folderPath);
    }

    @FXML
    private void detCreateScriptButtonClick(ActionEvent event) {
        File file = new File(saveToDetectionGroovyScript());
        qupath.getScriptEditor().showScript(file);
    }

    //define generateScriptName button action
    @FXML
    private void detGenerateScriptNameButtonClick(ActionEvent event) {
        updateDetectionScriptName();
    }

    private void updateDetectionScriptName() {
        //calculate downsample
        int downsample = 1;
        int level = Integer.parseInt(detLevelChoiceBox.getValue());
        downsample = (int) Math.pow(4, level);

        //get script name
        String scriptName = "ds_" + String.valueOf(downsample) +
                "_ps_size_" + detPatchSize.getText() +
                "_object_detection.groovy";

        //set scriptname
        detScriptName.setText(scriptName);
    }

    private String saveToDetectionGroovyScript() {
        try {

            String detection_script = new String(NeuroRObjectDetectionController.class
                    .getResourceAsStream("/qupath/lib/neuror/run_detection.groovy").readAllBytes());

            //Edit class names into appropriate string
            //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
            String classNamesText = detClassNames.getText();
            List<String> classes = Arrays.asList(classNamesText.split("\\s*,\\s*"));
            StringBuilder classNames = new StringBuilder();
            for (String i : classes) {
                if (classNames.length() != 0) {
                    classNames.append(",");
                }
                classNames.append("\"" + i + "\"");
            }

            String selectedClassNamesText = detSelectedClassNames.getText();
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
                    anacondaEnvPath.getText(),
                    pythonExecPath.getText().replace('\\','/'),
                    detExecPath.getText().replace('\\','/'),
                    detModelPath.getText().replace('\\','/'),
                    detImagePath.getText().replace('\\','/'),
                    detOutputPath.getText().replace('\\','/'),
                    detJsonExportPath.getText().replace('\\','/'),
                    detImgLibChoiceBox.getValue(), //img_lib
                    detPatchSize.getText(), //patch_size
                    detLevelChoiceBox.getValue(), //level
                    classNames.toString(), //className
                    detBatchSize.getText(), //batchSize
                    detNumGPUChoiceBox.getValue(), //num_gpus
                    selectedClassNames.toString() //selectedClassNames
            );

            String scriptName = detScriptName.getText();

            if (scriptName == "") {
                updateDetectionScriptName();
                scriptName = detScriptName.getText();
            }

            String scriptPath = buildFilePath(PROJECT_BASE_DIR, "scripts", scriptName);
            makePathInProject("scripts");
            try (PrintWriter writer = new PrintWriter(scriptPath, "UTF-8")) {
                writer.print(filled_detection_script);
            }
            return(scriptPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
