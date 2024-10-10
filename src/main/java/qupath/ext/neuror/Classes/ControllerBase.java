package qupath.ext.neuror.Classes;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import qupath.lib.gui.QuPathGUI;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qupath.lib.scripting.QP.*;

public class ControllerBase {

    protected Stage stage;

    // functions to handle path options
    // connects textfield text to stored stringProperty (persistentpreference)
    // connects button click to setting textfield text
    // model path -> try to parse Level (downsample)
    protected void handleModelPath(Button button, TextField textField, StringProperty stringProperty, ChoiceBox<String> choiceBox) {
        textField.setText(stringProperty.getValue());

        textField.textProperty().addListener((observable, oldText, newText) -> {
            stringProperty.setValue(newText);
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedPath = getFile(stringProperty.getValue());
                textField.textProperty().setValue(selectedPath);

                // parse model path for options
                if (selectedPath != null) {
                    String[] modelParseArray = selectedPath.split("_");
                    for (String str: modelParseArray) {
                        if (str.contains("ds")) {
                            String level = String.valueOf((int)(Math.log(Double.parseDouble(str.substring(2))) / Math.log(4)));
                            choiceBox.setValue(level);
                        }
                    }
                }
            }
        });
    }

    protected void handleFilePath(Button button, TextField textField, StringProperty stringProperty) {
        textField.setText(stringProperty.getValue());

        textField.textProperty().addListener((observable, oldText, newText) -> {
            stringProperty.setValue(newText);
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedPath = getFile(stringProperty.getValue()).replace(".exe", "");
                textField.textProperty().setValue(selectedPath);
            }
        });
    }

    protected void handleDirectoryPath(Button button, TextField textField, StringProperty stringProperty, Boolean initialDirectoryIsProjectDir) {
        textField.setText(stringProperty.getValue());

        textField.textProperty().addListener((observable, oldText, newText) -> {
            stringProperty.setValue(newText);
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedPath = "";
                if (!initialDirectoryIsProjectDir) {
                    selectedPath = getDirectory(stringProperty.getValue()) + "/";
                } else {
                    selectedPath = getDirectory(buildFilePath(PROJECT_BASE_DIR)) + "/";
                }
                textField.textProperty().setValue(selectedPath);
            }
        });
    }

    // functions to handle option options
    protected void handleOption(TextField textField, StringProperty stringProperty) {
        textField.setText(stringProperty.getValue());

        textField.textProperty().addListener((observable, oldText, newText) -> {
            stringProperty.setValue(newText);
        });
    }

    protected void handleOption(ChoiceBox<String> choiceBox, StringProperty stringProperty) {
        choiceBox.setValue(stringProperty.getValue());

        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stringProperty.setValue(choiceBox.getValue());
            }
        });
    }

    // get file path
    protected String getFile(String initialFilePath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        if (initialFilePath != null) {
            if (Files.exists(Paths.get(initialFilePath))) {
                File file = new File(initialFilePath);
                File directory = file.getParentFile();
                fileChooser.setInitialDirectory(directory);
            }
        }
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath().replace('\\', '/');
        } else {
            return null;
        }
    }

    // get directory path
    protected String getDirectory(String setInitialDirectory) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        if (setInitialDirectory != null) {
            if (Files.exists(Paths.get(setInitialDirectory))) {
                directoryChooser.setInitialDirectory(new File(buildFilePath(setInitialDirectory)));
            }
        }
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            return selectedDirectory.getAbsolutePath().replace('\\', '/');
        }
        else {
            return null;
        }
    }

    // create script file in project_dir/scripts
    protected String saveScript(StringProperty scriptName, String script) {
        String scriptPath = buildFilePath(PROJECT_BASE_DIR, "scripts", scriptName.getValue());
        makePathInProject("scripts");
        try (PrintWriter writer = new PrintWriter(scriptPath, "UTF-8")) {
            writer.print(script);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scriptPath;
    }

    // open script in scriptEditor
    protected void openScriptEditor(QuPathGUI qupath, String scriptPath) {
        File file = new File(scriptPath);
        qupath.getScriptEditor().showScript(file);
    }

    protected void errorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
