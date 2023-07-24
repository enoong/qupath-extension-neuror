module qupath.lib.neuror {
    requires javafx.controls;
    requires javafx.fxml;


    opens qupath.lib.neuror to javafx.fxml;
    exports qupath.lib.neuror;
}

