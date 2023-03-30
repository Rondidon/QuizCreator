package de.quizgamemaker.userinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import de.quizgamemaker.data.Program;
import de.quizgamemaker.ie.Exporter;
import de.quizgamemaker.ie.Importer;
import de.quizgamemaker.ie.IncompatibleVersionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;

public class SceneEditor implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) { }
	
	private ResourceBundle bundle = de.quizgamemaker.locales.Locales.getLanguageBundle();
	private Scene scene;
	
	@FXML private BorderPane root;
	@FXML private Button buttonNew;
	@FXML private Button buttonLoad;
	@FXML private Button buttonSave;
	@FXML private Button buttonPlay;
	@FXML private Button buttonPublish;
	@FXML private Button buttonWebshop;
	@FXML private MenuItem menuitemClose;
	@FXML private MenuItem menuitemSave;
	@FXML private MenuItem menuitemSaveAs;
	@FXML private MenuItem menuitemLoad;
	@FXML private MenuItem menuitemAboutUs;
	@FXML private ImageView imageNew;
	@FXML private ImageView imageOpen;
	@FXML private ImageView imageSave;
	@FXML private ImageView imagePlay;
	@FXML private ImageView imagePublish;
	@FXML private ImageView imageWebshop;
	@FXML private Tab tabCreateQuestionPacks;
	@FXML private Tab tabConfigureQuiz;
	@FXML private Tab tabManageMedia;
	@FXML private Menu menuLastEdited;
	
	/**
	 * Handles the button actions
	 * @param e
	 */
	@FXML
	private void handleButtonAction(ActionEvent e) {
		Object source = e.getSource();
		if(source == menuitemClose) {
			closeAction();
		}
		if(source == menuitemSave || source == buttonSave) {
			saveProject();
		}
		if(source == menuitemLoad || source == buttonLoad) {
			loadProject();
		}
		if(source == menuitemSaveAs) {
			saveProjectAs();
		}
		if(source == menuitemAboutUs) {
			openAboutUs();
		}
	}
	
	/**
	 * Refreshes the scene, sets the window title
	 */
	private void refreshScene() {
		root.setEffect(null);
		WindowManager.getStage("editorStage").setTitle(Program.getProject().getTitle().concat(" - Quiz Game Maker"));
		tabCreateQuestionPacks.setContent(new PaneCreateQuestionPacks().getPane());
		tabConfigureQuiz.setContent(new PaneConfigureQuiz().getPane());
		tabManageMedia.setContent(new PaneManageMedia().getPane());
		
		menuLastEdited.getItems().clear();
		
		// last edited projects
		String[] a = Program.getProjectLocations().keySet().toArray(new String[Program.getProjectLocations().keySet().size()]);
		for(int i=0; i<a.length; i++) {
			MenuItem item = new MenuItem();
			String location = a[i];
			item.setText(Program.getProjectLocations().get(a[i]) + "\n(" + location + ")");
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					loadProject(location);
				}
			});
			menuLastEdited.getItems().add(item);
		}
		
		if(Program.getProjectLocations().size() > 0) {
			menuLastEdited.setVisible(true);
			MenuItem menuitemClear = new MenuItem();
			menuitemClear.setText(bundle.getString("menubar_clearlist"));
			menuitemClear.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Program.getProjectLocations().clear();
					Exporter e = new Exporter();
					e.saveProgramSettings();
					refreshScene();
				}
			});
			menuLastEdited.getItems().add(menuitemClear);
		}
		else {
			menuLastEdited.setVisible(false);
		}
	}
	
	/**
	 * Opens and defines the About Us window
	 */
	private void openAboutUs() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		WindowManager.setBoxEffect("editorStage");
		
		try {
			alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("res/img/logo.png"))));
		} catch (Exception e) {
			System.out.println("Could not load: res/img/logo.png");
		}
		
		alert.setTitle(bundle.getString("title_aboutus"));
		alert.setHeaderText(bundle.getString("title_software").concat("\nVersion: " + Program.getVersion() + "\n" + "\u00a9" +  "2020 Kindler Software"));
		alert.getDialogPane().setPrefSize(400, 220);
		alert.setContentText(bundle.getString("text_aboutus"));
		ButtonType buttonClose = new ButtonType(bundle.getString("button_close"));
		ButtonType buttonWebsite = new ButtonType(bundle.getString("button_visitwebsite"));
		alert.getButtonTypes().setAll(buttonClose, buttonWebsite);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonWebsite) { 
			// showWebsite 
		}
		WindowManager.getStage("editorStage").getScene().getRoot().setEffect(null);
		alert.close();
	}
	
	/**
	 * Defines the project -> "load project" action 
	 */
	private void loadProject() {
		root.setEffect(WindowManager.getBoxEffect());
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(bundle.getString("title_close"));
		alert.setHeaderText(bundle.getString("text_closeproject1"));
		alert.setContentText(bundle.getString("text_closeproject2"));
		ButtonType buttonTypeSave = new ButtonType(bundle.getString("text_save"));
		ButtonType buttonTypeDontSave = new ButtonType(bundle.getString("text_dontsave"));
		ButtonType buttonTypeCancel = new ButtonType(bundle.getString("text_cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeSave){
			saveProject();
			root.setEffect(WindowManager.getBoxEffect());
		} else if(result.get() == buttonTypeDontSave) {
		   
		} else {
			root.setEffect(null);
			return;
		}
		// Location Chooser
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Projekt laden...");
				fileChooser.getExtensionFilters().addAll(
				        new ExtensionFilter("QuizGameMaker Projekt *.qgm", "*.qgm"));
				File selectedFile = fileChooser.showOpenDialog(WindowManager.getStage("editorStage"));
					if(selectedFile != null) {
						String location = selectedFile.toString();
						Importer i = new Importer();
						try {
							i.loadProject(location);
							refreshScene();
						} catch (IncompatibleVersionException e) {
							e.printStackTrace();
							Alert alert2 = new Alert(AlertType.ERROR);
							alert2.setTitle(bundle.getString("title_error"));
							alert2.setHeaderText(bundle.getString("text_erroroccurred"));
							alert2.setContentText(bundle.getString("text_unequalversion"));

							alert2.showAndWait();
							root.setEffect(null);
						}
					}
					else {
						root.setEffect(null);
					}
					
	}
	
	private void loadProject(String location) {
		root.setEffect(WindowManager.getBoxEffect());
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(bundle.getString("title_close"));
		alert.setHeaderText(bundle.getString("text_closeproject1"));
		alert.setContentText(bundle.getString("text_closeproject2"));
		ButtonType buttonTypeSave = new ButtonType(bundle.getString("text_save"));
		ButtonType buttonTypeDontSave = new ButtonType(bundle.getString("text_dontsave"));
		ButtonType buttonTypeCancel = new ButtonType(bundle.getString("text_cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeSave){
			saveProject();
			root.setEffect(WindowManager.getBoxEffect());
		} else if(result.get() == buttonTypeDontSave) {
		   
		} else {
			root.setEffect(null);
			return;
		}
		Importer i = new Importer();
		try {
			i.loadProject(location);
			refreshScene();
		} catch (IncompatibleVersionException e) {
			e.printStackTrace();
			Alert alert2 = new Alert(AlertType.ERROR);
			alert2.setTitle(bundle.getString("title_error"));
			alert2.setHeaderText(bundle.getString("text_erroroccurred"));
			alert2.setContentText(bundle.getString("text_unequalversion"));

			alert2.showAndWait();
			root.setEffect(null);
		}			
	}
	
	/**
	 * Defines the project -> "save project as" action. Opens a file chooser 
	 */
	private void saveProjectAs() {
		root.setEffect(WindowManager.getBoxEffect());
		// Location Chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Projekt speichern unter...");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("QuizGameMaker Projekt *.qgm", "*.qgm"));
				fileChooser.setInitialFileName(Program.getProject().getTitle().concat(".qgm"));
				File selectedFile = fileChooser.showSaveDialog(WindowManager.getStage("editorStage"));	
				if(selectedFile != null) {
					String location = selectedFile.toString();
					// Exporter
					Exporter exporter = new Exporter();
					exporter.saveProject(Program.getProject(), location);
					Program.getProject().setProjectLocation(location);
					Program.getProjectLocations().put( location, Program.getProject().getTitle());
					refreshScene();
				}
				root.setEffect(null);
	}
	
	/**
	 * Defines the project -> "save project as" action.
	 */
	private void saveProject() {
		// if project was not saved yet
		if(Program.getProject().getProjectLocation().equals("")) {
			saveProjectAs();
		}
		// if project has already been saved or was loaded before
		else {
			String location = Program.getProject().getProjectLocation();
			// Exporter
			Exporter exporter = new Exporter();
			exporter.saveProject(Program.getProject(), location);
			Program.getProject().setProjectLocation(location);
			Program.getProjectLocations().put( location, Program.getProject().getTitle());
		}
	}
	
	/**
	 * Defines the Project->Close action. Asks for saving current project before going back to the welcome screen
	 */
	private void closeAction() {
		root.setEffect(WindowManager.getBoxEffect());
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(bundle.getString("title_close"));
		alert.setHeaderText(bundle.getString("text_closeproject"));
		alert.setContentText(bundle.getString("text_closeproject2"));
		ButtonType buttonTypeSave = new ButtonType(bundle.getString("text_save"));
		ButtonType buttonTypeDontSave = new ButtonType(bundle.getString("text_dontsave"));
		ButtonType buttonTypeCancel = new ButtonType(bundle.getString("text_cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeSave) {
			saveProject();
			WindowManager.closeStage("editorStage");
			Stage welcomeStage = new Stage();
			WindowManager.addStage(welcomeStage, "welcomeStage");
			WindowManager.openWelcomeStage(welcomeStage, true);
		} else if(result.get() == buttonTypeDontSave) {
		    WindowManager.closeStage("editorStage");
		    Stage welcomeStage = new Stage();
		    WindowManager.addStage(welcomeStage, "welcomeStage");
		    WindowManager.openWelcomeStage(welcomeStage, true);
		} else {
			root.setEffect(null);
		}
	}
	
	/**
	 * Loads the localized scene along with all the layout, images, needed scripts  
	 * @return the scene 
	 */
	public Scene getScene() {
		try {
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneEditor.fxml"), bundle
			);
			loader.setController(this);
			scene = new Scene(loader.load());
		} catch (IOException e) {
			scene = null;
			e.printStackTrace();
		}
		
		try {
			imageNew.setImage(new Image(new FileInputStream("res/img/icon_new.png")));
			buttonNew.setTooltip(new Tooltip(bundle.getString("tooltip_newproject")));
			imageOpen.setImage(new Image(new FileInputStream("res/img/icon_open.png")));
			buttonLoad.setTooltip(new Tooltip(bundle.getString("tooltip_loadproject")));
			imageSave.setImage(new Image(new FileInputStream("res/img/icon_save.png")));
			buttonSave.setTooltip(new Tooltip(bundle.getString("tooltip_saveproject")));
			imagePlay.setImage(new Image(new FileInputStream("res/img/icon_play.png")));
			buttonPlay.setTooltip(new Tooltip(bundle.getString("tooltip_playproject")));
			imagePublish.setImage(new Image(new FileInputStream("res/img/icon_publish.png")));
			buttonPublish.setTooltip(new Tooltip(bundle.getString("tooltip_publishproject")));
			imageWebshop.setImage(new Image(new FileInputStream("res/img/icon_webshop.png")));
			buttonWebshop.setTooltip(new Tooltip(bundle.getString("tooltip_webshop")));
		} catch (FileNotFoundException e) {
			System.out.println("Could not load some icon png images in res/img/icon:xx.png");
		}
		
		WindowManager.getStage("editorStage").setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				closeAction();
			}
		});
		
		refreshScene();
		
		return scene;
	}
}
