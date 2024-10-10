package qupath.ext.neuror;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import qupath.ext.neuror.Classes.ControllerBase;
import qupath.lib.gui.QuPathGUI;

import static qupath.lib.scripting.QP.getProject;

public class ObjectDetectionController extends ControllerBase {

    public ObjectDetectionController(QuPathGUI qupath, EnvironmentModel envModel, ObjectDetectionModel model, ObjectDetectionView view) {

        if (envModel.getEnvObjectDetectionPath().getValue().isEmpty()) {
            errorAlert("Path to detection.py not set. Please set it in 'Environments' first.");
        }

        if (getProject() == null) {
            errorAlert("Please open a project before generating script!");
        }

        handleModelPath(view.getModelPath().getButton(), view.getModelPath().getTextField(), model.getDetModelPath(), view.getLevel().getChoiceBox());
        handleDirectoryPath(view.getImagesPath().getButton(), view.getImagesPath().getTextField(), model.getDetImagesPath());
        handleDirectoryPath(view.getOutputPath().getButton(), view.getOutputPath().getTextField(), model.getDetOutputPath());
        handleDirectoryPath(view.getJSONPath().getButton(), view.getJSONPath().getTextField(), model.getDetJSONPath());

        handleOption(view.getImgLib().getChoiceBox(), model.getDetImgLib());
        handleOption(view.getPatchSize().getTextField(), model.getDetPatchSize());
        handleOption(view.getLevel().getChoiceBox(), model.getDetLevel());
        handleOption(view.getBatchSize().getTextField(), model.getDetBatchSize());
        handleOption(view.getNumGPUs().getChoiceBox(), model.getDetNumGPUs());
        handleOption(view.getClasses().getTextField(), model.getDetClasses());
        handleOption(view.getROIClasses().getTextField(), model.getDetROIClasses());

        handleOption(view.getScriptName().getTextField(), model.getScriptName());

        // Generate script name from stored values
        view.getScriptName().getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int downsample = 1;
                int level = Integer.parseInt(model.getDetLevel().getValue());
                downsample = (int) Math.pow(4, level);

                String scriptName = "";

                if (!model.getDetROIClasses().getValue().isEmpty()) {
                    scriptName += "object_detection_roiONLY";
                } else {
                    scriptName += "object_detection";
                }

                scriptName += "_ds_" + String.valueOf(downsample) +
                        "_ps_" + model.getDetPatchSize().getValue() +
                        ".groovy";

                view.getScriptName().getTextField().setText(scriptName);
            }
        });

        // Create script
        view.getCreateScriptButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String scriptTemplatePath = "/qupath/ext/neuror/run_detection_v05_roi.groovy";
                createScript(qupath, scriptTemplatePath, model, envModel);
            }
        });

    }


    // function for creating script
    private void createScript(QuPathGUI qupath, String scriptTemplatePath, ObjectDetectionModel model, EnvironmentModel envModel) {
        //Edit class names into appropriate string
        //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
        StringBuilder classNamesText = new StringBuilder();
        String[] classNames = model.getDetClasses().getValue().split(",");
        for (String classname: classNames) {
            if (!classname.isEmpty() && !classNamesText.isEmpty()) {
                classNamesText.append(",");
            }
            classNamesText.append("\"").append(classname.strip()).append("\"");
        }
        String classes = classNamesText.toString();

        //Edit ROI Class names into appropriate string
        StringBuilder ROIClassNamesText = new StringBuilder();
        if (!model.getDetROIClasses().getValue().isEmpty()) {
            String[] ROIClassNames = model.getDetROIClasses().getValue().split(",");
            for (String className: ROIClassNames) {
                if (!className.isEmpty() && !ROIClassNamesText.isEmpty()) {
                    ROIClassNamesText.append(",");
                }
                ROIClassNamesText.append("\"").append(className.strip()).append("\"");
            }
        }
        String ROIClasses = ROIClassNamesText.toString();

        try {
            String objectDetectionScript = new String(ObjectDetectionController.class
                    .getResourceAsStream(scriptTemplatePath).readAllBytes());

            String filledObjectDetectionScript;

            //fill run_detection.groovy
            filledObjectDetectionScript = String.format(
                    objectDetectionScript,
                    envModel.getEnvAnacondaPath().getValue().substring(0, envModel.getEnvAnacondaPath().getValue().length() - 1), //remove trailing "/" from anaconda path
                    envModel.getEnvPythonPath().getValue().replace('\\', '/'),
                    envModel.getEnvObjectDetectionPath().getValue().replace('\\', '/'),
                    model.getDetModelPath().getValue().replace('\\', '/'),
                    model.getDetImagesPath().getValue().replace('\\', '/'),
                    model.getDetOutputPath().getValue().replace('\\', '/'),
                    model.getDetJSONPath().getValue(), //blank space for json export folder
                    model.getDetImgLib().getValue(), //img_lib
                    model.getDetPatchSize().getValue(), //patch_size
                    model.getDetLevel().getValue(), //level
                    classes, //className
                    model.getDetBatchSize().getValue(), //batchSize
                    model.getDetNumGPUs().getValue(), //num_gpus
                    ROIClasses //ROI class names
            );

            String scriptName = saveScript(model.getScriptName(), filledObjectDetectionScript);
            openScriptEditor(qupath, scriptName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
