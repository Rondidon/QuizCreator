package com.quizcreator.app.test;

import java.io.File;

import com.quizcreator.app.ie.Importer;
import com.quizcreator.app.services.projectLocation.ProjectLocationService;
import com.quizcreator.app.services.projectLocation.ProjectLocationServiceImpl;
import com.quizcreator.app.tools.FolderTools;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestImporter extends Application {
	final FolderTools folderTools = new FolderTools();
	final ProjectLocationService projectLocationService = new ProjectLocationServiceImpl(folderTools);


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
				Importer i = new Importer(folderTools, projectLocationService);
				i.loadProject(location);
			}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
