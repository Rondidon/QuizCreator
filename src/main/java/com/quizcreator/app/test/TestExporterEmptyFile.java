package com.quizcreator.app.test;

import com.quizcreator.app.data.*;
import com.quizcreator.app.ie.*;

import java.io.File;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TestExporterEmptyFile extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Project setup
		Project project = new Project("");
		
		// Location Chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Leeres Projekt speichern unter...");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("QuizGameMaker Projekt *.qgm", "*.qgm"));
		fileChooser.setInitialFileName(project.getTitle().concat(".qgm"));
		File selectedFile = fileChooser.showSaveDialog(primaryStage);	
		if(selectedFile != null) {
			String location = selectedFile.toString();
			
			// Exporter
			Exporter exporter = new Exporter();
			exporter.saveProject(project, location);
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

