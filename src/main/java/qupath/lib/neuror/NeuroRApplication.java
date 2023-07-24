package qupath.lib.neuror;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NeuroRApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        // 선택 사항: 컨트롤러 인스턴스에 접근해야 하는 경우, loader에서 가져올 수 있습니다.
        NeuroRController controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NeuroR GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

