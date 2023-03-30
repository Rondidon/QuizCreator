package de.quizgamemaker.userinterface;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class PaneManageMedia implements Initializable {
	private ResourceBundle bundle = de.quizgamemaker.locales.Locales.getLanguageBundle();
	
	@FXML BorderPane root;
	
	public BorderPane getPane() {
		try {
			// load FXML file
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("PaneManageMedia.fxml"), bundle
			);
			loader.setController(this);
			Scene scene = new Scene(loader.load());
			
			// return the root BorderPane
			root = (BorderPane) scene.getRoot();
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
