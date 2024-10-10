package qupath.ext.neuror;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import qupath.ext.neuror.Classes.ControllerBase;
import qupath.lib.gui.QuPathGUI;

import static qupath.lib.scripting.QP.getProject;


public class SegmentationController extends ControllerBase {

    public SegmentationController(QuPathGUI qupath, EnvironmentModel envModel, SegmentationModel model, SegmentationView view) {

        if (envModel.getEnvSegmentationPath().getValue() == "") {
            errorAlert("Path to segmentation.py not set. Please set it in 'Environments' first.");
        }

        if (getProject() == null) {
            errorAlert("Please open a project before generating script!");
        }

        handleModelPath(view.getModelPath().getButton(), view.getModelPath().getTextField(), model.getSegModelPath(), view.getLevel().getChoiceBox());
        handleDirectoryPath(view.getImagesPath().getButton(), view.getImagesPath().getTextField(), model.getSegImagesPath());
        handleDirectoryPath(view.getOutputPath().getButton(), view.getOutputPath().getTextField(), model.getSegOutputPath());
        handleDirectoryPath(view.getJSONPath().getButton(), view.getJSONPath().getTextField(), model.getSegJSONPath());

        handleOption(view.getImgLib().getChoiceBox(), model.getSegImgLib());
        handleOption(view.getPatchSize().getTextField(), model.getSegPatchSize());
        handleOption(view.getOverlap().getTextField(), model.getSegOverlap());
        handleOption(view.getLevel().getChoiceBox(), model.getSegLevel());
        handleOption(view.getBatchSize().getTextField(), model.getSegBatchSize());
        handleOption(view.getNumGPUs().getChoiceBox(), model.getSegNumGPUs());
        handleOption(view.getClasses().getTextField(), model.getSegClasses());
        handleOption(view.getROIClasses().getTextField(), model.getSegROIClasses());

        handleOption(view.getScriptName().getTextField(), model.getScriptName());

        // Generate script name from stored values
        view.getScriptName().getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int downsample = 1;
                int level = Integer.parseInt(model.getSegLevel().getValue());
                downsample = (int) Math.pow(4, level);

                String scriptName = "";

                if (model.getSegROIClasses().getValue() != "") {
                    scriptName += "segmentation_roiONLY";
                } else {
                    scriptName += "segmentation";
                }

                scriptName += "_ds_" + String.valueOf(downsample) +
                        "_ps_" + model.getSegPatchSize().getValue() +
                        "_ov_" + model.getSegOverlap().getValue() +
                        ".groovy";

                view.getScriptName().getTextField().setText(scriptName);
            }
        });

        // Create script
        view.getCreateScriptButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String scriptTemplatePath = "/qupath/ext/neuror/run_segmentation_v05_roi.groovy";
                createScript(qupath, scriptTemplatePath, model, envModel);
            }
        });

    }


    // function for creating script
    private void createScript(QuPathGUI qupath, String scriptTemplatePath, SegmentationModel model, EnvironmentModel envModel) {
        //Edit class names into appropriate string
        //e.g. "Tumor, Cells, Immune" -> "Tumor", "Cells", "Immune"
        StringBuilder classNamesText = new StringBuilder();
        String[] classNames = model.getSegClasses().getValue().split(",");
        for (String className: classNames) {
            if (!className.isEmpty() && !classNamesText.isEmpty()) {
                classNamesText.append(",");
            }
            classNamesText.append("\"").append(className.strip()).append("\"");
        }
        String classes = classNamesText.toString();

        //Edit ROI Class names into appropriate string
        StringBuilder ROIClassNamesText = new StringBuilder();
        if (!model.getSegROIClasses().getValue().isEmpty()) {
            String[] ROIClassNames = model.getSegROIClasses().getValue().split(",");
            for (String className: ROIClassNames) {
                if (!className.isEmpty() && !ROIClassNamesText.isEmpty()) {
                    ROIClassNamesText.append(",");
                }
                ROIClassNamesText.append("\"").append(className.strip()).append("\"");
            }
        }
        String ROIClasses = ROIClassNamesText.toString();

        try {
            System.out.println(SegmentationController.class);
            String segmentationScript = new String(SegmentationController.class
                    .getResourceAsStream(scriptTemplatePath).readAllBytes());

            String filledSegmentationScript;

            //fill run_segmentation_roi.groovy
            filledSegmentationScript = String.format(
                    segmentationScript,
                    envModel.getEnvAnacondaPath().getValue().substring(0, envModel.getEnvAnacondaPath().getValue().length() - 1), //remove trailing "/" from anaconda path
                    envModel.getEnvPythonPath().getValue().replace('\\', '/'),
                    envModel.getEnvSegmentationPath().getValue().replace('\\', '/'),
                    model.getSegModelPath().getValue().replace('\\', '/'),
                    model.getSegImagesPath().getValue().replace('\\', '/'),
                    model.getSegOutputPath().getValue().replace('\\', '/'),
                    model.getSegJSONPath().getValue(), //blank space for json export folder
                    model.getSegImgLib().getValue(), //img_lib
                    model.getSegPatchSize().getValue(), //patch_size
                    model.getSegLevel().getValue(), //level
                    classes, //className
                    model.getSegOverlap().getValue(), //overlap
                    model.getSegBatchSize().getValue(), //batchSize
                    model.getSegNumGPUs().getValue(), //num_gpus
                    ROIClasses //ROI class names
            );

            String scriptPath = saveScript(model.getScriptName(), filledSegmentationScript);
            openScriptEditor(qupath, scriptPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
