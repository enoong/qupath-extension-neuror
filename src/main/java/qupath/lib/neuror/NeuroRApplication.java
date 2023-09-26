package qupath.lib.neuror;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qupath.lib.gui.QuPathGUI;

import java.io.IOException;

public class NeuroRApplication {

    private QuPathGUI qupath;

    NeuroRApplication (QuPathGUI qupath) {
        this.qupath = qupath;
    }

     void showNeuroRSegmentationOptions() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new NeuroRSegmentationController(qupath));
        loader.setLocation(NeuroRApplication.class.getResource("/qupath/lib/neuror/NeuroR_Segmentation.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 선택 사항: 컨트롤러 인스턴스에 접근해야 하는 경우, loader에서 가져올 수 있습니다.
        //NeuroRController controller = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("NeuroR Segmentation");
        stage.show();
    }
    void showNeuroRObjectDetectionOptions() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new NeuroRObjectDetectionController(qupath));
        loader.setLocation(NeuroRApplication.class.getResource("/qupath/lib/neuror/NeuroR_Object_Detection.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 선택 사항: 컨트롤러 인스턴스에 접근해야 하는 경우, loader에서 가져올 수 있습니다.
        //NeuroRController controller = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("NeuroR Object Detection");
        stage.show();
    }

    void showNeuroRGUIOptions() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new NeuroRGUIController(qupath));
        loader.setLocation(NeuroRApplication.class.getResource("/qupath/lib/neuror/NeuroR_GUI.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 선택 사항: 컨트롤러 인스턴스에 접근해야 하는 경우, loader에서 가져올 수 있습니다.
        //NeuroRController controller = loader.getController();
        NeuroRGUIController controller = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("NeuroR GUI");
        stage.show();

    }

    /*
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NeuroR_Segmentation.fxml"));
        Parent root = loader.load();

        // 선택 사항: 컨트롤러 인스턴스에 접근해야 하는 경우, loader에서 가져올 수 있습니다.
        NeuroRController controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NeuroR GUI");
        primaryStage.show();
    }

    public static void openGUI(String[] args) {
        launch(args);
    }

     */
}

