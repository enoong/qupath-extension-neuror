package qupath.ext.neuror;

import javafx.beans.property.StringProperty;
import qupath.lib.gui.prefs.PathPrefs;

public class ClassificationModel {

    // paths
    private static final StringProperty clsModelPath = PathPrefs.createPersistentPreference("clsModelPath", "");
    public StringProperty getClsModelPath() {
        return clsModelPath;
    }
    private static final StringProperty clsImagesPath = PathPrefs.createPersistentPreference("clsImagesPath", "");
    public StringProperty getClsImagesPath() {
        return clsImagesPath;
    }
    private static final StringProperty clsOutputPath = PathPrefs.createPersistentPreference("clsOutputPath", "");
    public StringProperty getClsOutputPath() {
        return clsOutputPath;
    }
    private static final StringProperty clsJSONPath = PathPrefs.createPersistentPreference("clsJSONPath", "");
    public StringProperty getClsJSONPath() {
        return clsJSONPath;
    }
    // options
    private static final StringProperty clsImgLib = PathPrefs.createPersistentPreference("clsImgLib", "openslide");
    public StringProperty getClsImgLib() {
        return clsImgLib;
    }
    private static final StringProperty clsPatchSize = PathPrefs.createPersistentPreference("clsPatchSize", "256");
    public StringProperty getClsPatchSize() {
        return clsPatchSize;
    }
    private static final StringProperty clsOverlap = PathPrefs.createPersistentPreference("clsOverlap", "64");
    public StringProperty getClsOverlap() {
        return clsOverlap;
    }
    private static final StringProperty clsLevel = PathPrefs.createPersistentPreference("clsLevel", "1");
    public StringProperty getClsLevel() {
        return clsLevel;
    }
    private static final StringProperty clsBatchSize = PathPrefs.createPersistentPreference("clsBatchSize", "128");
    public StringProperty getClsBatchSize() {
        return clsBatchSize;
    }
    private static final StringProperty clsNumGPUs = PathPrefs.createPersistentPreference("clsNumGPUs", "1");
    public StringProperty getClsNumGPUs() {
        return clsNumGPUs;
    }
    // if empty -> use manualInputClass.groovy
    // else -> use autoImportClass.groovy
    private static final StringProperty clsClasses = PathPrefs.createPersistentPreference("clsClasses", "");
    public StringProperty getClsClasses() {
        return clsClasses;
    }
    // if not empty -> only run for ROIs with these names
    private static final StringProperty clsROIClasses = PathPrefs.createPersistentPreference("clsROIClasses", "");
    public StringProperty getClsROIClasses() {
        return clsROIClasses;
    }
    // if empty -> set gradeMode = false
    // else -> set gradeMode = true, ArrayList grades = grades
    private static final StringProperty clsGrades = PathPrefs.createPersistentPreference("clsGrades", "");
    public StringProperty getClsGrades() {
        return clsGrades;
    }

    private static final StringProperty scriptName = PathPrefs.createPersistentPreference("scriptName", "");
    public StringProperty getScriptName() {
        return scriptName;
    }
}
