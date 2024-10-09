package qupath.ext.neuror;

import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qupath.lib.common.Version;
import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.extensions.GitHubProject;
import qupath.lib.gui.extensions.QuPathExtension;

public class Extension_NeuroR implements QuPathExtension, GitHubProject{

    private static final Logger logger = LoggerFactory.getLogger(Extension_NeuroR.class);
    private static final String EXTENSION_NAME = "NeuroR extension";
    private static final String EXTENSION_DESCRIPTION = "GUI extension for easy NeuroR inference";
    private static final Version EXTENSION_QUPATH_VERSION = Version.parse("v0.5.1");
    private static final GitHubRepo EXTENSION_REPOSITORY = GitHubRepo.create(
            EXTENSION_NAME, "enoong", "qupath-neuror-extension");
    private boolean isInstalled = false;



    //--------------------------
    // Object Detection Fields
    //--------------------------

    @Override
    public void installExtension(QuPathGUI qupath) {
        if (isInstalled) {
            logger.debug("{} is already installed", getName());
            return;
        }
        isInstalled = true;
        addEnvironmentMenu(qupath);
        addSegmentationMenu(qupath);
        addObjectDetectionMenu(qupath);
        addClassificationMenu(qupath);
    }

    private void addEnvironmentMenu(QuPathGUI qupath) {
        var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
        MenuItem segmentationMenu = new MenuItem("Environments");
        segmentationMenu.setOnAction(e -> createEnvironmentStage(qupath));
        menu.getItems().add(segmentationMenu);
    }

    private void createEnvironmentStage(QuPathGUI qupath) {
        Stage stage = new Stage();
        EnvironmentView view = new EnvironmentView(qupath);

        Scene scene = new Scene(view);

        stage.setScene(scene);
        stage.setTitle("NeuroR - Environments");
        stage.show();
    }


    private void addSegmentationMenu(QuPathGUI qupath) {
        var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
        MenuItem segmentationMenu = new MenuItem("Segmentation");
        segmentationMenu.setOnAction(e -> createSegmentationStage(qupath));
        menu.getItems().add(segmentationMenu);
    }

    private void createSegmentationStage(QuPathGUI qupath) {
        Stage stage = new Stage();
        //load and send environment variables to segmentation menu
        EnvironmentModel envModel = new EnvironmentModel(qupath);

        SegmentationView view = new SegmentationView(qupath, envModel);
        Scene scene = new Scene(view);

        stage.setScene(scene);
        stage.setTitle("NeuroR - Segmentation");
        stage.show();
    }

    private void addObjectDetectionMenu(QuPathGUI qupath) {
        var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
        MenuItem objectDetectionMenu = new MenuItem("Object Detection");
        objectDetectionMenu.setOnAction(e -> createObjectDetectionStage(qupath));
        menu.getItems().add(objectDetectionMenu);
    }

    private void createObjectDetectionStage(QuPathGUI qupath) {
        Stage stage = new Stage();
        //load and send environment variables to segmentation menu
        EnvironmentModel envModel = new EnvironmentModel(qupath);

        ObjectDetectionView view = new ObjectDetectionView(qupath, envModel);
        Scene scene = new Scene(view);

        stage.setScene(scene);
        stage.setTitle("NeuroR - Object Detection");
        stage.show();
    }

    private void addClassificationMenu(QuPathGUI qupath) {
        var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
        MenuItem classificationMenu = new MenuItem("Classification");
        classificationMenu.setOnAction(e -> createClassificationStage(qupath));
        menu.getItems().add(classificationMenu);
    }

    private void createClassificationStage(QuPathGUI qupath) {
        Stage stage = new Stage();
        //load and send environment variables to segmentation menu
        EnvironmentModel envModel = new EnvironmentModel(qupath);

        ClassificationView view = new ClassificationView(qupath, envModel);
        Scene scene = new Scene(view);

        stage.setScene(scene);
        stage.setTitle("NeuroR - Classification");
        stage.show();
    }


    @Override
    public String getName() {
        return EXTENSION_NAME;
    }

    @Override
    public String getDescription() {
        return EXTENSION_DESCRIPTION;
    }

    @Override
    public Version getQuPathVersion() {
        return EXTENSION_QUPATH_VERSION;
    }

    @Override
    public GitHubRepo getRepository() {
        return EXTENSION_REPOSITORY;
    }
}
