package qupath.ext.neuror.Classes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class PathOptions {

    private final Label label = new Label();
    private final TextField textField = new TextField();
    private final Button button = new Button("...");

    public PathOptions(String labelText) {
        label.setText(labelText);
    }

    public PathOptions(String labelText, String tooltipText) {
        label.setText(labelText);
        Tooltip tooltip = new Tooltip(tooltipText);
        label.setTooltip(tooltip);
    }

    public TextField getTextField() {
        return textField;
    }

    public Button getButton() {
        return button;
    }

    // Function to add row to GridPane
    public void addToGridPane(GridPane pathGrid, int row) {
        pathGrid.add(label, 0, row);
        pathGrid.add(textField, 1, row);
        pathGrid.add(button, 2, row);
    }
}
