package de.quizgamemaker.userinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import de.quizgamemaker.data.Program;
import de.quizgamemaker.ie.Exporter;
import de.quizgamemaker.ie.Importer;
import de.quizgamemaker.ie.IncompatibleVersionException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class SceneWelcome implements Initializable {
	private ResourceBundle bundle = de.quizgamemaker.locales.Locales.getLanguageBundle();
	
	@FXML private Button buttonNewProject;
	@FXML private ImageView imageNewProject;
	@FXML private Label labelNewProject;
	
	@FXML private Button buttonOpenProject;
	@FXML private ImageView imageOpenProject;
	@FXML private Label labelOpenProject;
	
	@FXML private Button buttonManual;
	@FXML private ImageView imageManual;
	@FXML private Label labelManual;
	
	@FXML private Button buttonVideotut;
	@FXML private ImageView imageVideotut;
	@FXML private Label labelVideotut;
	
	@FXML private Button buttonLanguage;
	@FXML private ImageView imageLanguage;
	@FXML private Label labelLanguage;
	
	@FXML private ImageView imageLogo;
	@FXML private Label labelQuickstart;
	@FXML private Label labelLastEdited;
	@FXML private Label labelWelcome;
	@FXML private Label labelFooter;
	
	@FXML private VBox vboxLastEdited;
	
	private Scene scene = null;
	
	/**
	 * Refreshes the resourceBundle, logo images and texts . Needed in getScene and after a language change
	 */
	private void refresh() {
		de.quizgamemaker.locales.Locales.setLanguageBundle(ResourceBundle.getBundle("de.quizgamemaker.locales.LanguageBundle", Locale.getDefault()));
		bundle = de.quizgamemaker.locales.Locales.getLanguageBundle();
		labelNewProject.setText(bundle.getString("button_newproject"));
		labelOpenProject.setText(bundle.getString("button_openproject"));
		labelManual.setText(bundle.getString("button_manual"));
		labelVideotut.setText(bundle.getString("button_videotut"));
		labelLanguage.setText(bundle.getString("button_language"));
		labelQuickstart.setText(bundle.getString("label_quickstart"));
		labelLastEdited.setText(bundle.getString("label_lastedited"));
		labelWelcome.setText(bundle.getString("label_welcome"));
		
		WindowManager.setStageTitle("welcomeStage", bundle.getString("title_welcome"));
		
		// language logo for German
		if(Locale.getDefault() == Locale.GERMAN) {
			Image i;
			try {
				i = new Image(new FileInputStream("res/img/icon_german.png"));
				imageLanguage.setImage(i);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_german.png");
			}
		}
		// language logo for English
		else {
			Image i;
			try {
				i = new Image(new FileInputStream("res/img/icon_uk.png"));
				imageLanguage.setImage(i);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_uk.png");
			}
		}
		
		//build text for labelFooter
		labelFooter.setText(bundle.getString("fragment_version") + " " + Program.getVersion() + ". ");
		
	}
	
	
	/**
	 * Action for the "Open project" button in the welcome screen.
	 * Opens a fileChooser and delivers the location to loadProject(location)
	 */
	private void loadProject() {
		
		WindowManager.setBoxEffect("welcomeStage");
	
		// Location Chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Projekt laden...");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("QuizGameMaker Projekt *.qgm", "*.qgm"));
		File selectedFile = fileChooser.showOpenDialog(WindowManager.getStage("welcomeStage"));
		if(selectedFile != null) {
			String location = selectedFile.toString();
			loadProject(location);
		}
		else {
			WindowManager.getStage("welcomeStage").getScene().getRoot().setEffect(null);
		}
	}
	
	/**
	 * Action for loading a project with a known location. Needed after the location is chosen 
	 * in loadProject() and for the "last edited projects" list. Every button there has loadProject(location)
	 * as action assigned.
	 * @param location
	 */
	private void loadProject(String location) {
		Importer i = new Importer();
		try {
			i.loadProject(location);
			WindowManager.closeStage("welcomeStage");
			Stage s = new Stage();
			WindowManager.addStage(s, "editorStage");
			SceneEditor e = new SceneEditor();
			s.setScene(e.getScene());
			s.centerOnScreen();
			s.show();
			s.setTitle(Program.getProject().getTitle().concat(" - " + bundle.getString("title_software")));
		} catch (IncompatibleVersionException e) {
			e.printStackTrace();
			Alert alert2 = new Alert(AlertType.ERROR);
			alert2.setTitle(bundle.getString("title_error"));
			alert2.setHeaderText(bundle.getString("text_erroroccurred"));
			alert2.setContentText(bundle.getString("text_unequalversion"));
			alert2.showAndWait();
			WindowManager.getStage("welcomeStage").getScene().getRoot().setEffect(null);
		}
	}

	/**
	 * Button event handler
	 * @param e
	 */
	@FXML
	protected void handleButtonAction(ActionEvent e) {
		if(e.getSource() == buttonNewProject) {
			if(Program.DEBUG) System.out.println("New Project");
			SceneNewProject npc = new SceneNewProject();
			Stage newprojectStage = new Stage();
			Stage welcomeStage = WindowManager.getStage("welcomeStage");
			WindowManager.addStage(newprojectStage, "newProjectStage");
			welcomeStage.setTitle(bundle.getString("title_newproject"));
			newprojectStage.setScene(npc.getScene());
			newprojectStage.setResizable(false);
			newprojectStage.setTitle(bundle.getString("title_newproject"));
			newprojectStage.setAlwaysOnTop(true);
			newprojectStage.initModality(Modality.WINDOW_MODAL);
			newprojectStage.initOwner(welcomeStage);
			
			WindowManager.setBoxEffect("welcomeStage");
			
			newprojectStage.show();
			WindowManager.bindStagePositionRelativeToParent(newprojectStage, welcomeStage);
		}
		if(e.getSource() == buttonOpenProject) {
			if(Program.DEBUG) System.out.println("Open Project");
			loadProject();
		}
		if(e.getSource() == buttonManual) {
			System.out.println("Not implemented: Manual");
		}
		if(e.getSource() == buttonVideotut) {
			System.out.println("Not implemented: Video Tutorials");
		}
		if(e.getSource() == buttonLanguage) {
			// German -> English
			if(Locale.getDefault() == Locale.GERMAN) {
				// Change language
				Locale.setDefault(Locale.ENGLISH);
				if(Program.DEBUG) System.out.println("Language change: English");
			}
			// English -> German
			else {
				Locale.setDefault(Locale.GERMAN);
				if(Program.DEBUG) System.out.println("Language change: German");
			}
			refresh();
			Exporter exporter = new Exporter();
			exporter.saveProgramSettings();
		}
	}
	
	private void refreshLastEditedProjects() {
		// last edited projects
		vboxLastEdited.getChildren().clear();
		String[] a = Program.getProjectLocations().keySet().toArray(new String[Program.getProjectLocations().keySet().size()]);
		for(int i=0; i<a.length; i++) {
			Button button = new Button();
			String location = a[i];
			button.setText(Program.getProjectLocations().get(a[i]) + "\n(" + location + ")");
			button.setMinHeight(48);
			button.setPrefWidth(Double.MAX_VALUE);
			button.maxWidth(Double.MAX_VALUE);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					loadProject(location);
				}
			});
			vboxLastEdited.getChildren().add(button);
		}
		if(Program.getProjectLocations().keySet().size() > 0) {
			Button buttonClear = new Button();
			buttonClear.setText(bundle.getString("menubar_clearlist"));
			buttonClear.setPrefWidth(50000);
			buttonClear.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Program.getProjectLocations().clear();
					Exporter e = new Exporter();
					e.saveProgramSettings();
					refreshLastEditedProjects();
				}
			});
			vboxLastEdited.getChildren().add(buttonClear);
		}
	}
	
	/**
	 * Loads the localized scene along with all the layout, images, needed scripts...
	 * @param resetProgram Whether the Program (and Quiz) shall be reset 
	 * @return the scene
	 */
	public Scene getScene(Boolean resetProgram) {
		try {
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneWelcome.fxml"), bundle
			);
			loader.setController(this);
			scene = new Scene(loader.load());
			
			if(resetProgram) {
				Program.setupProgram();
			}
			
			// load images
			Image i;
			try {
				i = new Image(new FileInputStream("res/img/icon_new.png"));
				imageNewProject.setImage(i);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_new.png");
			}
			try {
				i = new Image(new FileInputStream("res/img/icon_open.png"));
				imageOpenProject.setImage(i);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_open.png");
			}
			try {
				i = new Image(new FileInputStream("res/img/icon_manual.png"));
				imageManual.setImage(i);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_manual.png");
			}
			try {
				i = new Image(new FileInputStream("res/img/icon_videotut.png"));
				imageVideotut.setImage(i);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/icon_videotut.png");
			}
			try {
				i = new Image(new FileInputStream("res/img/logo.png"));
				imageLogo.setImage(i);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not load: res/img/logo.png");
			}
			
			refresh();
			refreshLastEditedProjects();
			return scene ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
