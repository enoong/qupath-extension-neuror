package qupath.ext.neuror.Classes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class OutputOptions {
    private final Label label = new Label();
    private final TextField textField = new TextField();
    private final Button button = new Button();

    public OutputOptions(String labelText, String tooltipText, String buttonText) {
        label.setText(labelText);
        Tooltip tooltip = new Tooltip(tooltipText);
        label.setTooltip(tooltip);
        button.setText(buttonText);
    }

    public TextField getTextField() {
        return textField;
    }

    public Button getButton() {
        return button;
    }

    public void addToGridPane(GridPane pathGrid, int row) {
        pathGrid.add(label, 0, row);
        pathGrid.add(textField, 1, row);
        pathGrid.add(button, 2, row);
    }
}
