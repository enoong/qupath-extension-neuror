package qupath.ext.neuror;

import javafx.beans.property.StringProperty;
import qupath.lib.gui.prefs.PathPrefs;

public class SegmentationModel {

    // paths
    private static final StringProperty segModelPath = PathPrefs.createPersistentPreference("segModelPath", "");
    public StringProperty getSegModelPath() {
        return segModelPath;
    }
    private static final StringProperty segImagesPath = PathPrefs.createPersistentPreference("segImagesPath", "");
    public StringProperty getSegImagesPath() {
        return segImagesPath;
    }
    private static final StringProperty segOutputPath = PathPrefs.createPersistentPreference("segOutputPath", "");
    public StringProperty getSegOutputPath() {
        return segOutputPath;
    }
    private static final StringProperty segJSONPath = PathPrefs.createPersistentPreference("segJSONPath", "");
    public StringProperty getSegJSONPath() {
        return segJSONPath;
    }
    // options
    private static final StringProperty segImgLib = PathPrefs.createPersistentPreference("segImgLib", "openslide");
    public StringProperty getSegImgLib() {
        return segImgLib;
    }
    private static final StringProperty segPatchSize = PathPrefs.createPersistentPreference("segPatchSize", "256");
    public StringProperty getSegPatchSize() {
        return segPatchSize;
    }
    private static final StringProperty segOverlap = PathPrefs.createPersistentPreference("segOverlap", "64");
    public StringProperty getSegOverlap() {
        return segOverlap;
    }
    private static final StringProperty segLevel = PathPrefs.createPersistentPreference("segLevel", "1");
    public StringProperty getSegLevel() {
        return segLevel;
    }
    private static final StringProperty segBatchSize = PathPrefs.createPersistentPreference("segBatchSize", "128");
    public StringProperty getSegBatchSize() {
        return segBatchSize;
    }
    private static final StringProperty segNumGPUs = PathPrefs.createPersistentPreference("segNumGPUs", "2");
    public StringProperty getSegNumGPUs() {
        return segNumGPUs;
    }
    private static final StringProperty segClasses = PathPrefs.createPersistentPreference("segClasses", "");
    public StringProperty getSegClasses() {
        return segClasses;
    }
    // if not empty -> only run for ROIs with these names
    private static final StringProperty segROIClasses = PathPrefs.createPersistentPreference("segROIClasses", "");
    public StringProperty getSegROIClasses() {
        return segROIClasses;
    }

    private static final StringProperty scriptName = PathPrefs.createPersistentPreference("scriptName", "");
    public StringProperty getScriptName() {
        return scriptName;
    }

}
