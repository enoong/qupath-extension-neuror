package qupath.ext.neuror;

import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import qupath.ext.neuror.Classes.PathOptions;
import qupath.lib.gui.QuPathGUI;

public class EnvironmentView extends VBox {

    private final PathOptions envAnacondaPath = new PathOptions("Anaconda", "Path to anaconda directory.");
    private final PathOptions envPythonPath = new PathOptions("Python", "Path to python executable.");
    private final PathOptions envSegmentationPath = new PathOptions("Segmentation", "Path to segmentation.py (e.g. neuro_seg_v3.py)");
    private final PathOptions envObjectDetectionPath = new PathOptions("Object Detection", "Path to object_detection.py (e.g. neuro_det_v3.py)");
    private final PathOptions envClassificationPath = new PathOptions("Classification", "Path to classification.py (e.g. nrt_cla.py)");

    public PathOptions getEnvAnacondaPath() { return envAnacondaPath; }
    public PathOptions getEnvPythonPath() { return envPythonPath; }
    public PathOptions getEnvSegmentationPath() { return envSegmentationPath; }
    public PathOptions getEnvObjectDetectionPath() { return envObjectDetectionPath; }
    public PathOptions getEnvClassificationPath() { return envClassificationPath; }

    public EnvironmentView(QuPathGUI qupath) {

        EnvironmentModel model = new EnvironmentModel(qupath);
        EnvironmentController controller = new EnvironmentController(model, this);

        GridPane pathGrid = new GridPane();
        pathGrid.setPadding(new Insets(5, 0, 5, 5));
        pathGrid.setHgap(5);

        ColumnConstraints pathCol1 = new ColumnConstraints();
        ColumnConstraints pathCol2 = new ColumnConstraints();
        ColumnConstraints pathCol3 = new ColumnConstraints();
        pathCol2.setHgrow(Priority.ALWAYS);
        pathGrid.getColumnConstraints().addAll(pathCol1, pathCol2, pathCol3);

        int pathRow = 0;
        envAnacondaPath.addToGridPane(pathGrid, pathRow++);
        envPythonPath.addToGridPane(pathGrid, pathRow++);
        envSegmentationPath.addToGridPane(pathGrid, pathRow++);
        envObjectDetectionPath.addToGridPane(pathGrid, pathRow++);
        envClassificationPath.addToGridPane(pathGrid, pathRow++);

        this.getChildren().addAll(pathGrid);

    }

}
