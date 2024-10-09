package qupath.ext.neuror;

import javafx.beans.property.StringProperty;
import qupath.fx.prefs.controlsfx.PropertyItemBuilder;
import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.prefs.PathPrefs;

public class EnvironmentModel {

    private final StringProperty envAnacondaPath = PathPrefs.createPersistentPreference("envAnacondaPath", "");
    private final StringProperty envPythonPath = PathPrefs.createPersistentPreference("envPythonPath", "");
    private final StringProperty envSegmentationPath = PathPrefs.createPersistentPreference("envSegmentationPath", "");
    private final StringProperty envObjectDetectionPath = PathPrefs.createPersistentPreference("envObjectDetectionPath", "");
    private final StringProperty envClassificationPath = PathPrefs.createPersistentPreference("envClassificationPath", "");

    public StringProperty getEnvAnacondaPath() { return envAnacondaPath; }
    public StringProperty getEnvPythonPath() { return envPythonPath; }
    public StringProperty getEnvSegmentationPath() { return envSegmentationPath; }
    public StringProperty getEnvObjectDetectionPath() { return envObjectDetectionPath; }
    public StringProperty getEnvClassificationPath() { return envClassificationPath; }

    private void addStringPropertyToPreferences(QuPathGUI qupath, StringProperty stringProperty, String name, String category, String description) {
        var propertyItem = new PropertyItemBuilder<>(stringProperty, String.class)
                .name(name)
                .category(category)
                .description(description)
                .build();
        qupath.getPreferencePane()
                .getPropertySheet()
                .getItems()
                .add(propertyItem);
    }

    public EnvironmentModel(QuPathGUI qupath) {
        String category = new String("Neuro-R extension environment paths");
        addStringPropertyToPreferences(qupath, envAnacondaPath, "Anaconda Path", category, "Path to anaconda directory.");
        addStringPropertyToPreferences(qupath, envPythonPath, "Python Path", category, "Path to python executable.");
        addStringPropertyToPreferences(qupath, envSegmentationPath, "Segmentation Path", category, "Path to segmentation.py");
        addStringPropertyToPreferences(qupath, envObjectDetectionPath, "Object Detection Path", category, "Path to object_detection.py");
        addStringPropertyToPreferences(qupath, envClassificationPath, "Classification Path", category, "Path to classification.py");
    }

}
