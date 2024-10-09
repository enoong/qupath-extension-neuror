package qupath.ext.neuror.Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.util.List;

public class OptionOptions {
    private final Label label = new Label();
    private final TextField textField = new TextField();
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

    public OptionOptions(String labelText) {
        label.setText(labelText);
    }

    public OptionOptions(String labelText, String tooltipText) {
        label.setText(labelText);
        Tooltip tooltip = new Tooltip(tooltipText);
        label.setTooltip(tooltip);
    }

    public OptionOptions(String labelText, String tooltipText, List<String> choices) {
        label.setText(labelText);
        Tooltip tooltip = new Tooltip(tooltipText);
        label.setTooltip(tooltip);

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(choices);

        choiceBox.getItems().addAll(observableList);
    }

    public TextField getTextField() {
        return textField;
    }

    public ChoiceBox<String> getChoiceBox() {
        return choiceBox;
    }

    public void addToGridPane(GridPane pathGrid, int row) {
        pathGrid.add(label, 0, row);
        if (!choiceBox.getItems().isEmpty()) {
            pathGrid.add(choiceBox, 1, row);
        } else {
            pathGrid.add(textField, 1, row);
        }
    }

}
