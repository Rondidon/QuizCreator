package de.quizgamemaker.test;
import java.io.File;

import de.quizgamemaker.data.*;
import de.quizgamemaker.ie.Exporter;
import de.quizgamemaker.ie.Importer;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestAudioImport extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Program.setupProgram();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Audio Datei importieren...");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Alle Audio Dateien", "*.mp3", "*.wav", "*.aif", "*.aiff"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		if(selectedFile != null) {
			String location = selectedFile.toString();
			Importer i = new Importer(); 
			i.importAudioFile(location, selectedFile.getName());
		}
		Exporter e = new Exporter();
		e.saveProject(Program.getProject(), "/home/robin/Schreibtisch/blabla.qgm");
		
		System.exit(0);
	}
}