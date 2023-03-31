package com.quizcreator.app.userinterface;

import com.quizcreator.app.data.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneNewProject implements Initializable {
	private ResourceBundle bundle = com.quizcreator.app.i18n.Locales.getGUIBundle();
	private Scene scene;
	
	@FXML private Button buttonBack;
	@FXML private Button buttonCreate;
	@FXML private ImageView imageLogo;
	@FXML private TextField textfieldTitle;
	@FXML private TextField textfieldAuthor;
	@FXML private ChoiceBox<String> choiceboxGameMode;

	@Override
	public void initialize(URL location, ResourceBundle resources) { }
	
	/**
	 * Button event handler
	 * @param e
	 */
	@FXML
	protected void handleButtonAction(ActionEvent e) {
		if(e.getSource() == buttonBack) {
			if(Program.DEBUG) System.out.println("Back");
			closeStage();
		}
		if(e.getSource() == buttonCreate) {
			if(Program.DEBUG) System.out.println("Create");
			Program.getProject().setTitle(textfieldTitle.getText());
			Program.getProject().setAuthor(textfieldAuthor.getText());
			Quiz quiz = new Quiz();
			switch (choiceboxGameMode.getSelectionModel().getSelectedIndex()) {
			case 0:
				quiz.setGameMode(new GameModeSimple());
				break;
			case 1:
				quiz.setGameMode(new GameModePoints());
				break;
			case 2:
				quiz.setGameMode(new GameModeTime());
				break;
			case 3:
				quiz.setGameMode(new GameModeMillionaire());
				break;
			}
			WindowManager.closeStage("newProjectStage");
			WindowManager.closeStage("welcomeStage");
			Stage editorStage = new Stage();
			WindowManager.addStage(editorStage, "editorStage");
			SceneEditor editorScene = new SceneEditor();
			editorStage.setScene(editorScene.getScene());
			editorStage.setTitle(Program.getProject().getTitle().concat(" - " + bundle.getString("title_software")));
			editorStage.centerOnScreen();
			editorStage.show();
		}
	}
	
	/**
	 * closes the stage
	 */
	private void closeStage() {
		WindowManager.setStageTitle("welcomeStage", bundle.getString("title_welcome"));
		WindowManager.closeStage("newProjectStage");
		WindowManager.setNullEffect("welcomeStage");
	}
	
	/**
	 * checks if create button can be disabled 
	 * (= input values of the form are complete)
	 */
	private void checkDisableCreateButton() {
		if(textfieldTitle.getText().length()>0
				&& textfieldAuthor.getText().length()>0) 
		{
			buttonCreate.setDisable(false);
		}
	}
	
	private String checkRegex(String text) {
		if(text.length() > 48) {
			text = text.substring(0, 48);
		}
		if(!text.matches("[A-Za-z0-9äÄöÖüÜß,.@:/\\-][A-Za-z0-9äÄöÖüÜß,.@\\u0020:/\\-]*")) {
			if(text.length() > 0) {
				if(text.charAt(0) == ' ') {
					text=text.substring(1,text.length());
				}
				else {
					text=text.substring(0,text.length()-1);
				}
			}
			else { text=""; }
		}
		return text;
	}
	
	/**
	 * Returns the Welcome Screen Scene
	 */
	public Scene getScene() {
		try {
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneNewProject.fxml"), bundle
			);
			loader.setController(this);
			scene = new Scene(loader.load());
		} catch (IOException e) {
			scene = null;
			e.printStackTrace();
		}
		// Fill the Choice box with choices
		ObservableList<String> list = FXCollections.observableArrayList();
		list = FXCollections.observableArrayList(
				bundle.getString("choicebox_simplequiz"),
				bundle.getString("choicebox_pointquiz"),
				bundle.getString("choicebox_timequiz"),
				bundle.getString("choicebox_millionairequiz"));

		choiceboxGameMode.setItems(list);
		choiceboxGameMode.setValue(list.get(0));
		buttonCreate.setDisable(true);
	
		textfieldTitle.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				textfieldTitle.setText(checkRegex(textfieldTitle.getText()));
				checkDisableCreateButton();
			}
		});
		
		textfieldAuthor.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				textfieldAuthor.setText(checkRegex(textfieldAuthor.getText()));
				checkDisableCreateButton();
			}
		});
		
		choiceboxGameMode.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				checkDisableCreateButton();
			}

		});
		
		// logo image for the header
		try {
			Image i = new Image(new FileInputStream("res/img/logo.png"));
			imageLogo.setImage(i);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not load: res/img/logo.png");
		}
		
		// stage close event handler
		WindowManager.getStage("newProjectStage").setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				closeStage();
			}
		});
		
		return scene;
	}
	
}
