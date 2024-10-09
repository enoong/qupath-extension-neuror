package qupath.ext.neuror;

import javafx.beans.property.StringProperty;
import qupath.lib.gui.prefs.PathPrefs;

public class ObjectDetectionModel {
    private static final StringProperty detModelPath = PathPrefs.createPersistentPreference("detModelPath", "");
    public StringProperty getDetModelPath() {
        return detModelPath;
    }
    private static final StringProperty detImagesPath = PathPrefs.createPersistentPreference("detImagesPath", "");
    public StringProperty getDetImagesPath() {
        return detImagesPath;
    }
    private static final StringProperty detOutputPath = PathPrefs.createPersistentPreference("detOutputPath", "");
    public StringProperty getDetOutputPath() {
        return detOutputPath;
    }
    private static final StringProperty detJSONPath = PathPrefs.createPersistentPreference("detJSONPath", "");
    public StringProperty getDetJSONPath() {
        return detJSONPath;
    }
    // options
    private static final StringProperty detImgLib = PathPrefs.createPersistentPreference("detImgLib", "openslide");
    public StringProperty getDetImgLib() {
        return detImgLib;
    }
    private static final StringProperty detPatchSize = PathPrefs.createPersistentPreference("detPatchSize", "256");
    public StringProperty getDetPatchSize() {
        return detPatchSize;
    }
    private static final StringProperty detLevel = PathPrefs.createPersistentPreference("detLevel", "1");
    public StringProperty getDetLevel() {
        return detLevel;
    }
    private static final StringProperty detBatchSize = PathPrefs.createPersistentPreference("detBatchSize", "128");
    public StringProperty getDetBatchSize() {
        return detBatchSize;
    }
    private static final StringProperty detNumGPUs = PathPrefs.createPersistentPreference("detNumGPUs", "2");
    public StringProperty getDetNumGPUs() {
        return detNumGPUs;
    }
    private static final StringProperty detClasses = PathPrefs.createPersistentPreference("detClasses", "");
    public StringProperty getDetClasses() {
        return detClasses;
    }
    // if not empty -> only run for ROIs with these names
    private static final StringProperty detROIClasses = PathPrefs.createPersistentPreference("detROIClasses", "");
    public StringProperty getDetROIClasses() {
        return detROIClasses;
    }

    private static final StringProperty scriptName = PathPrefs.createPersistentPreference("scriptName", "");
    public StringProperty getScriptName() {
        return scriptName;
    }
}
