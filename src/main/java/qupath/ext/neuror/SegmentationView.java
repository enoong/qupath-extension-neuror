package qupath.ext.neuror;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import qupath.ext.neuror.Classes.PathOptions;
import qupath.ext.neuror.Classes.OptionOptions;
import qupath.ext.neuror.Classes.OutputOptions;
import qupath.lib.gui.QuPathGUI;


import java.util.List;

public class SegmentationView extends VBox {

    //-------------------
    // path GUI elements
    //-------------------
    private final PathOptions modelPath = new PathOptions("Model Path", "Path to model trained using Neuro-T.");
    private final PathOptions imagesPath = new PathOptions("Images Path", "Path to folder images to run inference on.");
    private final PathOptions outputPath = new PathOptions("Output Path", "Path where generated inference outputs will be saved.");
    private final PathOptions JSONPath = new PathOptions("JSON Path", "ath where generated JSON outputs will be saved.");
    public PathOptions getModelPath() { return modelPath; }
    public PathOptions getImagesPath() { return imagesPath; }
    public PathOptions getOutputPath() { return outputPath; }
    public PathOptions getJSONPath() { return JSONPath; }

    //-----------------
    // option elements
    //-----------------
    private final OptionOptions imgLib = new OptionOptions(
            "Img Library", "Library for reading images.\nOpenslide: .svs, .tif\nBioformats: ?\ndicom: dicom\nBlank: .jpeg, .png, .bmp, ...",
            List.of("openslide", "bioformats", "dicom", "")
    );
    private final OptionOptions patchSize = new OptionOptions(
            "Patch Size", "Images will be cut into squares of this size before inference.\nUsing the size of patches the model was trained on is recommended."
    );
    private final OptionOptions overlap = new OptionOptions(
            "Overlap", "How much overlap each cut patch will have."
    );
    private final OptionOptions level = new OptionOptions(
            "Level", "How zoomed out the images will be.\n0: 400x, 1: 100x, 2: 40x? 25x?",
            List.of("0", "1", "2", "3")
    );
    private final OptionOptions batchSize = new OptionOptions(
            "Batch Size", "How many patches will be processed simultaneously."
    );
    private final OptionOptions numGPUs = new OptionOptions(
            "Number of GPUs", "How many GPUs will be used (0: CPU).",
            List.of("0", "1", "2")
    );
    private final OptionOptions classes = new OptionOptions(
            "Classes", "Names of classes to be labeled, separated by commas (e.g. 'Tumor, Eosinophils' (without quotes))"
    );
    private final OptionOptions ROIClasses = new OptionOptions(
            "ROI Classes", "Only ROIs with these names will be used for inference.\nLeave empty to run on the whole image."
    );

    public OptionOptions getImgLib() { return imgLib; }
    public OptionOptions getPatchSize() { return patchSize; }
    public OptionOptions getOverlap() { return overlap; }
    public OptionOptions getLevel() { return level; }
    public OptionOptions getBatchSize() { return batchSize; }
    public OptionOptions getNumGPUs() { return numGPUs; }
    public OptionOptions getClasses() { return classes; }
    public OptionOptions getROIClasses() { return ROIClasses; };

    //outputs
    private final OutputOptions scriptName = new OutputOptions(
            "Script Name", "Click \"Generate\" button to automatically generate a name.", "Generate"
    );
    private final Button createScriptButton = new Button("Create Script!");

    public OutputOptions getScriptName() { return scriptName; };
    public Button getCreateScriptButton() { return createScriptButton; }

    //-----------------------
    // Add elements to View
    //-----------------------
    public SegmentationView(QuPathGUI qupath, EnvironmentModel envModel) {

        SegmentationModel model = new SegmentationModel();
        SegmentationController controller = new SegmentationController(qupath, envModel, model, this);

        // path options
        GridPane pathGrid = new GridPane();
        pathGrid.setPadding(new Insets(5, 0, 5, 5));
        pathGrid.setHgap(5);

        ColumnConstraints pathCol1 = new ColumnConstraints();
        ColumnConstraints pathCol2 = new ColumnConstraints();
        ColumnConstraints pathCol3 = new ColumnConstraints();
        pathCol2.setHgrow(Priority.ALWAYS);
        pathGrid.getColumnConstraints().addAll(pathCol1, pathCol2, pathCol3);

        int pathRow = 0;
        modelPath.addToGridPane(pathGrid, pathRow++);
        imagesPath.addToGridPane(pathGrid, pathRow++);
        outputPath.addToGridPane(pathGrid, pathRow++);
        JSONPath.addToGridPane(pathGrid, pathRow++);

        // option (non-path) options
        GridPane optionsGrid = new GridPane();
        optionsGrid.setPadding(new Insets(5, 5, 5, 5));
        optionsGrid.setHgap(5);

        ColumnConstraints optionsCol1 = new ColumnConstraints();
        ColumnConstraints optionsCol2 = new ColumnConstraints();
        optionsCol2.setHgrow(Priority.ALWAYS);
        optionsGrid.getColumnConstraints().addAll(optionsCol1, optionsCol2);

        int optionRow = 0;
        imgLib.addToGridPane(optionsGrid, optionRow++);
        patchSize.addToGridPane(optionsGrid, optionRow++);
        overlap.addToGridPane(optionsGrid, optionRow++);
        level.addToGridPane(optionsGrid, optionRow++);
        batchSize.addToGridPane(optionsGrid, optionRow++);
        numGPUs.addToGridPane(optionsGrid, optionRow++);
        classes.addToGridPane(optionsGrid, optionRow++);
        ROIClasses.addToGridPane(optionsGrid, optionRow++);

        // Script name textfield, generate button, create script button
        GridPane outputGrid = new GridPane();
        outputGrid.setPadding(new Insets(5, 0, 5, 5));
        outputGrid.setHgap(5);

        ColumnConstraints outputCol1 = new ColumnConstraints();
        ColumnConstraints outputCol2 = new ColumnConstraints();
        ColumnConstraints outputCol3 = new ColumnConstraints();
        outputCol2.setHgrow(Priority.ALWAYS);
        outputCol2.setHalignment(HPos.CENTER);
        outputGrid.getColumnConstraints().addAll(outputCol1, outputCol2, outputCol3);

        int outputRow = 1;
        scriptName.addToGridPane(outputGrid, outputRow++);
        outputGrid.add(createScriptButton, 1, outputRow + 1);

        this.getChildren().addAll(pathGrid, optionsGrid, outputGrid);
    }

}
