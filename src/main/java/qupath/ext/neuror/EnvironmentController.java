package qupath.ext.neuror;

import qupath.ext.neuror.Classes.ControllerBase;

public class EnvironmentController extends ControllerBase {

    public EnvironmentController(EnvironmentModel model, EnvironmentView view) {

        handleDirectoryPath(view.getEnvAnacondaPath().getButton(), view.getEnvAnacondaPath().getTextField(), model.getEnvAnacondaPath());
        handleFilePath(view.getEnvPythonPath().getButton(), view.getEnvPythonPath().getTextField(), model.getEnvPythonPath());
        handleFilePath(view.getEnvSegmentationPath().getButton(), view.getEnvSegmentationPath().getTextField(), model.getEnvSegmentationPath());
        handleFilePath(view.getEnvObjectDetectionPath().getButton(), view.getEnvObjectDetectionPath().getTextField(), model.getEnvObjectDetectionPath());
        handleFilePath(view.getEnvClassificationPath().getButton(), view.getEnvClassificationPath().getTextField(), model.getEnvClassificationPath());

    }
}
