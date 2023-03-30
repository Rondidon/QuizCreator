/**
 * Manages the Stages, opens and closes Windows. 
 * Every stage is saved in the internal HashMap "stages".
 */

package de.quizgamemaker.userinterface;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import de.quizgamemaker.data.Program;
import de.quizgamemaker.ie.Exporter;
import de.quizgamemaker.ie.Importer;
import de.quizgamemaker.ie.IncompatibleVersionException;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.stage.Stage;

public class WindowManager {
	
	// possible values: welcomeStage, addQuestionStage, editorStage
	private static HashMap<String,Stage> stages = new HashMap<String,Stage>();
	
	/**
	 * Registers a new stage in the Window Manager.
	 * Adds a new entry to a HashMap containing all stages. 
	 * @param stage The stage to add
	 * @param key The key/title of the stage. Needed to be able to find the stage again (getStage).
	 */
	public static void addStage(Stage stage, String key) {
		stages.put(key, stage);
	}
	
	/**
	 * Returns a registered stage in the Window Manager.
	 * @param key The key value of the stage given in addStage
	 * @return the stage
	 */
	public static Stage getStage(String key) {
		return stages.get(key);
	}
	
	/**
	 * Removes a stage from the internal HashMap containing all stages
	 * @param key The key value of the stage given in addStage
	 */
	public static void removeStage(String key) {
		stages.remove(key);
	}
	
	/**
	 * Checks if a specified stage is registered in the internal HashMap containing all stages
	 * @param key The key value of the stage given in addStage
	 * @return TRUE if it exists, FALSE if not
	 */
	public static boolean existsStage(String key) {
		return stages.containsKey(key);
	}
	
	/**
	 * Sets the stage title
	 * @param stage the stage the title shall be reset 
	 * @param title the title to set as String
	 */
	public static void setStageTitle(String key, String title) {
		getStage(key).setTitle(title);
	}
	
	public static void closeStage(String key) {
		Stage s = getStage(key);
		stages.remove(key);
		s.close();
	}
	
	/**
	 * Centers a stage in the middle of another stage
	 * @param child the parent to set the position to
	 * @param parent the stage in which the child shall be centered
	 */
	public static void bindStagePositionRelativeToParent(Stage child, Stage parent) {
		child.setX(parent.getX()+(parent.getWidth()/2)-(child.getWidth()/2));
		child.setY(parent.getY()+(parent.getHeight()/2)-(child.getHeight()/2));
		parent.xProperty().addListener((obs,oldVal,newVal) -> child.setX(parent.getX()+(parent.getWidth()/2)-(child.getWidth()/2)));
		parent.yProperty().addListener((obs, oldVal, newVal) -> child.setY(parent.getY()+(parent.getHeight()/2)-(child.getHeight()/2)));
	}
	
	public static Effect getBoxEffect() {
		BoxBlur bb = new BoxBlur();
        bb.setWidth(2);
        bb.setHeight(2);
        bb.setIterations(2);
        return bb;
	}
	
	/**
	 * Sets a box effect at a stage's scene's root node
	 * @param key the stages hash map key
	 */
	public static void setBoxEffect(String key) {
		getStage(key).getScene().getRoot().setEffect(getBoxEffect());
	}
	
	/**
	 * Resets effects at a stage's scene's root node
	 * @param key the stages hash map key
	 */
	public static void setNullEffect(String key) {
		getStage(key).getScene().getRoot().setEffect(null);
	}
	
	/**
	 * Opens the Welcome Screen
	 * @param programReset TRUE if the Program data (Quiz data etc) shall be reset. Useful after close project or at first start
	 */
	public static void openWelcomeStage(Stage stage, Boolean programReset) {
		SceneWelcome ws = new SceneWelcome();
		Importer i = new Importer();
		try {
			i.loadProgramSettings();
		} catch (IncompatibleVersionException e) {
			if(System.getProperty("user.language").equals("de")) {
				Locale.setDefault(Locale.GERMAN);
			}
			else {
				Locale.setDefault(Locale.ENGLISH);
			}
			Exporter exporter = new Exporter();
			exporter.saveProgramSettings();
		}
		stage.setScene(ws.getScene(programReset));
		ResourceBundle bundle = de.quizgamemaker.locales.Locales.getLanguageBundle();
		stage.setTitle(bundle.getString("title_welcome"));
		stage.setResizable(false);
		stage.show();
		System.out.println(Program.getProjectLocations().toString());
	}
	
}
