package qupath.ext.neuror;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import qupath.ext.neuror.Classes.ControllerBase;
import qupath.lib.gui.QuPathGUI;

import java.util.Objects;

import static qupath.lib.scripting.QP.getProject;

public class ClassificationController extends ControllerBase {

    public ClassificationController(QuPathGUI qupath, EnvironmentModel envModel, ClassificationModel model, ClassificationView view) {

        if (envModel.getEnvClassificationPath().getValue() == "") {
            errorAlert("Path to classification.py not set. Please set it in 'Environments' first.");
        }

        if (getProject() == null) {
            errorAlert("Please open a project before generating script!");
        }

        handleModelPath(view.getModelPath().getButton(), view.getModelPath().getTextField(), model.getClsModelPath(), view.getLevel().getChoiceBox());
        handleDirectoryPath(view.getImagesPath().getButton(), view.getImagesPath().getTextField(), model.getClsImagesPath());
        handleDirectoryPath(view.getOutputPath().getButton(), view.getOutputPath().getTextField(), model.getClsOutputPath());
        handleDirectoryPath(view.getJSONPath().getButton(), view.getJSONPath().getTextField(), model.getClsJSONPath());

        handleOption(view.getImgLib().getChoiceBox(), model.getClsImgLib());
        handleOption(view.getPatchSize().getTextField(), model.getClsPatchSize());
        handleOption(view.getOverlap().getTextField(), model.getClsOverlap());
        handleOption(view.getLevel().getChoiceBox(), model.getClsLevel());
        handleOption(view.getBatchSize().getTextField(), model.getClsBatchSize());
        handleOption(view.getNumGPUs().getChoiceBox(), model.getClsNumGPUs());
        handleOption(view.getClasses().getTextField(), model.getClsClasses());
        handleOption(view.getROIClasses().getTextField(), model.getClsROIClasses());
        handleOption(view.getGrades().getTextField(), model.getClsGrades());

        handleOption(view.getScriptName().getTextField(), model.getScriptName());

        // Generate script name from stored values
        view.getScriptName().getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int downsample = 1;
                int level = Integer.parseInt(model.getClsLevel().getValue());
                downsample = (int) Math.pow(4, level);

                String scriptName = "";

                if (model.getClsROIClasses().getValue() != null) {
                    scriptName += "classification_roiONLY";
                } else {
                    scriptName += "classification";
                }

                scriptName += "_ds_" + String.valueOf(downsample) +
                        "_ps_" + model.getClsPatchSize().getValue() +
                        "_ov_" + model.getClsOverlap().getValue() +
                        ".groovy";

                view.getScriptName().getTextField().setText(scriptName);
            }
        });

        // Create script
        view.getCreateScriptButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String scriptTemplatePath = "/qupath/ext/neuror/run_classification_v05_roi";
                Boolean classesInput;
                if (Objects.equals(model.getClsClasses().getValue(), "")) {
                    scriptTemplatePath += "_autoImportClass.groovy";
                    classesInput = Boolean.FALSE;
                } else {
                    scriptTemplatePath += "_manualInputClass.groovy";
                    classesInput = Boolean.TRUE;
                }
                createScript(qupath, scriptTemplatePath, model, envModel, classesInput);
            }
        });

    }


    // function for creating script
    private void createScript(QuPathGUI qupath, String scriptTemplatePath, ClassificationModel model, EnvironmentModel envModel, Boolean classesInput) {
        //Edit class names into appropriate string
        //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
        StringBuilder classNamesText = new StringBuilder();
        String[] classNames = model.getClsClasses().getValue().split(",");
        for (String className: classNames) {
            if (!className.isEmpty() && !classNamesText.isEmpty()) {
                classNamesText.append(",");
            }
            classNamesText.append("\"").append(className.strip()).append("\"");
        }
        String classes = classNamesText.toString();

        //Edit ROI Class names into appropriate string
        StringBuilder ROIClassNamesText = new StringBuilder();
        if (!model.getClsROIClasses().getValue().isEmpty()) {
            String[] ROIClassNames = model.getClsROIClasses().getValue().split(",");
            for (String className: ROIClassNames) {
                if (!className.isEmpty() && !ROIClassNamesText.isEmpty()) {
                    ROIClassNamesText.append(",");
                }
                ROIClassNamesText.append("\"").append(className.strip()).append("\"");
            }
        }
        String ROIClasses = ROIClassNamesText.toString();

        //Grades: empty -> gradeMode = false;
        String gradeMode = "";
        if (Objects.equals(model.getClsGrades().getValue(), "")) {
            gradeMode = "false";
        } else {
            gradeMode = "true";
        }

        try {
            String classificationScript = new String(ClassificationController.class
                    .getResourceAsStream(scriptTemplatePath).readAllBytes());

            String filledClassificationScript;

            //fill run_classification_roi.groovy
            //manual import
            if (classesInput == Boolean.TRUE) {
                filledClassificationScript = String.format(
                        classificationScript,
                        envModel.getEnvAnacondaPath().getValue(),
                        envModel.getEnvClassificationPath().getValue().replace('\\', '/'),
                        model.getClsImagesPath().getValue().replace('\\', '/'),
                        model.getClsModelPath().getValue().replace('\\', '/'),
                        model.getClsOutputPath().getValue().replace('\\', '/'),
                        model.getClsJSONPath().getValue(), //blank space for json export folder
                        model.getClsImgLib().getValue(), //img_lib
                        model.getClsNumGPUs().getValue(), //num_gpus
                        ROIClasses, //ROI class names
                        classes, //className
                        model.getClsLevel().getValue(), //level
                        model.getClsOverlap().getValue(), //overlap
                        model.getClsBatchSize().getValue(), //batchSize
                        model.getClsPatchSize().getValue(), //patch_size
                        gradeMode,
                        model.getClsGrades().getValue()
                );
            } else {
                //auto input
                filledClassificationScript = String.format(
                        classificationScript,
                        envModel.getEnvAnacondaPath().getValue(),
                        envModel.getEnvPythonPath().getValue(),
                        envModel.getEnvClassificationPath().getValue().replace('\\', '/'),
                        model.getClsImagesPath().getValue().replace('\\', '/'),
                        model.getClsModelPath().getValue().replace('\\', '/'),
                        model.getClsOutputPath().getValue().replace('\\', '/'),
                        model.getClsJSONPath().getValue(), //blank space for json export folder
                        model.getClsImgLib().getValue(), //img_lib
                        model.getClsNumGPUs().getValue(), //num_gpus
                        ROIClasses, //ROI class names
                        model.getClsLevel().getValue(), //level
                        model.getClsOverlap().getValue(), //overlap
                        model.getClsBatchSize().getValue(), //batchSize
                        model.getClsPatchSize().getValue(), //patch_size
                        gradeMode,
                        model.getClsGrades().getValue()
                );

            }

            String scriptPath = saveScript(model.getScriptName(), filledClassificationScript);
            openScriptEditor(qupath, scriptPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
