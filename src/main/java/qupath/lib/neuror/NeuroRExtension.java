package qupath.lib.neuror;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qupath.lib.common.Version;
import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.extensions.QuPathExtension;
import qupath.lib.gui.prefs.PathPrefs;


public class NeuroRExtension implements QuPathExtension {

	private final static Logger logger = LoggerFactory.getLogger(NeuroRExtension.class);

	/**
	 * Display name for your extension
	 */
	private final static String EXTENSION_NAME = "NeuroR";

	/**
	 * Short description, used under 'Extensions > Installed extensions'
	 */
	private final static String EXTENSION_DESCRIPTION = "Graphical user interface for NeuroR for use in QuPath";

	/**
	 * QuPath version that the extension is designed to work with.
	 * This allows QuPath to inform the user if it seems to be incompatible.
	 */
	private final static Version EXTENSION_QUPATH_VERSION = Version.parse("v0.4.3");

	/**
	 * Flag whether the extension is already installed (might not be needed... but we'll do it anyway)
	 */
	private boolean isInstalled = false;

	/**
	 * A 'persistent preference' - showing how to create a property that is stored whenever QuPath is closed
	 */
	private BooleanProperty enableExtensionProperty = PathPrefs.createPersistentPreference(
			"enableExtension", true);

	static StringProperty anacondaEnvPathProperty = PathPrefs.createPersistentPreference(
			"anacondaEnvPath", "D:/Anaconda3/envs/qupath");

	static StringProperty pythonExecPathProperty = PathPrefs.createPersistentPreference(
			"pythonExecPath", "D:/Anaconda3/envs/qupath/python" );

	static StringProperty neurorSegmentationExecPath = PathPrefs.createPersistentPreference(
			"neurorSegmentationExecPath", "" );

	static StringProperty neurorObjectDetectionExecPath = PathPrefs.createPersistentPreference(
			"neurorObjectDetectionExecPath", "" );

	public static StringProperty anacondaEnvPathProperty() {
		return anacondaEnvPathProperty;
	}

	public static StringProperty pythonExecPathProperty() {
		return pythonExecPathProperty;
	}

	public static StringProperty neurorSegmentationExecPathProperty() {
		return neurorSegmentationExecPath;
	}

	public static StringProperty neurorObjectDetectionExecPathProperty() { return neurorObjectDetectionExecPath; }

	@Override
	public void installExtension(QuPathGUI qupath) {
		if (isInstalled) {
			logger.debug("{} is already installed", getName());
			return;
		}
		isInstalled = true;
		addPreference(qupath);
		addMenuItem(qupath);
		addMenuItem1(qupath);
		addMenuItem2(qupath);
	}

	/**
	 * Demo showing how to add a persistent preference to the QuPath preferences pane.
	 * @param qupath
	 */
	private void addPreference(QuPathGUI qupath) {
		qupath.getPreferencePane().addPropertyPreference(
				enableExtensionProperty,
				Boolean.class,
				"Enable extension",
				EXTENSION_NAME,
				"Enable my extension");
		qupath.getPreferencePane().addPropertyPreference(
				anacondaEnvPathProperty,
				String.class,
				"Anaconda enviroment path",
				EXTENSION_NAME,
				"Set anaconda environment path (e.g. D:/Anaconda3/envs/qupath)"
		);
		qupath.getPreferencePane().addPropertyPreference(
				pythonExecPathProperty,
				String.class,
				"Python executable path",
				EXTENSION_NAME,
				"Set python executable path (e.g. D:/Anaconda3/envs/qupath/python)"
		);
		qupath.getPreferencePane().addPropertyPreference(
				neurorSegmentationExecPath,
				String.class,
				"NeuroR Segmentation executable path",
				EXTENSION_NAME,
				"Set NeuroR Segmentation executable path"
		);
		qupath.getPreferencePane().addPropertyPreference(
				neurorObjectDetectionExecPath,
				String.class,
				"NeuroR Object Detection executable path",
				EXTENSION_NAME,
				"Set NeuroR Object Detection executable path"
		);
	}

	/**
	 * Demo showing how a new command can be added to a QuPath menu.
	 * @param qupath
	 */
	private void addMenuItem(QuPathGUI qupath) {
		var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
		MenuItem menuItem = new MenuItem("Segmentation");
		NeuroRApplication neuroRApplication = new NeuroRApplication(qupath);
		menuItem.setOnAction(e -> {
			//code to call NeuroRApplication
			neuroRApplication.showNeuroRSegmentationOptions();
		});
		menuItem.disableProperty().bind(enableExtensionProperty.not());
		menu.getItems().add(menuItem);
	}

	private void addMenuItem1(QuPathGUI qupath) {
		var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
		MenuItem menuItem = new MenuItem("Object Detection");
		NeuroRApplication neuroRApplication = new NeuroRApplication(qupath);
		menuItem.setOnAction(e -> {
			//code to call NeuroRApplication
			neuroRApplication.showNeuroRObjectDetectionOptions();
		});
		menuItem.disableProperty().bind(enableExtensionProperty.not());
		menu.getItems().add(menuItem);
	}

	private void addMenuItem2(QuPathGUI qupath) {
		var menu = qupath.getMenu("Extensions>" + EXTENSION_NAME, true);
		MenuItem menuItem = new MenuItem("NeuroR GUI");
		NeuroRApplication neuroRApplication = new NeuroRApplication(qupath);
		menuItem.setOnAction(e -> {
			//code to call NeuroRApplication
			neuroRApplication.showNeuroRGUIOptions();
		});
		menuItem.disableProperty().bind(enableExtensionProperty.not());
		menu.getItems().add(menuItem);
	}

	@Override
	public String getName() {
		return EXTENSION_NAME;
	}

	@Override
	public String getDescription() {
		return EXTENSION_DESCRIPTION;
	}
	
	@Override
	public Version getQuPathVersion() {
		return EXTENSION_QUPATH_VERSION;
	}

}
