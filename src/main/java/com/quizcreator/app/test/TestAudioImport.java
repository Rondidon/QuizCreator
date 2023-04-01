package com.quizcreator.app.test;

import com.quizcreator.app.QuizCreatorApplication;
import com.quizcreator.app.ie.Exporter;
import com.quizcreator.app.ie.Importer;
import com.quizcreator.app.services.projectLocation.ProjectLocationServiceImpl;
import com.quizcreator.app.tools.FolderTools;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class TestAudioImport extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final var folderTools = new FolderTools();
		final var projectLocationService = new ProjectLocationServiceImpl(folderTools);

		// QuizCreatorApplication.setupCleanProject();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Audio Datei importieren...");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Alle Audio Dateien", "*.mp3", "*.wav", "*.aif", "*.aiff"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		if(selectedFile != null) {
			String location = selectedFile.toString();
			Importer i = new Importer(folderTools, projectLocationService);
			i.importAudioFile(location, selectedFile.getName());
		}
		Exporter e = new Exporter();
		e.saveProject(QuizCreatorApplication.getProject(), "/home/robin/Schreibtisch/blabla.qgm");
		
		System.exit(0);
	}
}