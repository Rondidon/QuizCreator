package de.quizgamemaker.test;

import java.io.File;

import de.quizgamemaker.ie.Importer;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestImporter extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Location Chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Projekt laden...");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("QuizGameMaker Projekt *.qgm", "*.qgm"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if(selectedFile != null) {
				String location = selectedFile.toString();
				Importer i = new Importer();
				i.loadProject(location);
			}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
