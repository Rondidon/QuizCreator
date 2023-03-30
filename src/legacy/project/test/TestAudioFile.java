package de.quizgamemaker.test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

import de.quizgamemaker.data.AudioFile;;

/**
 * A little AudioFile Media Player to test AudioFile.java 
 * @author Robin Kindler
 *
 */
public class TestAudioFile extends Application {
	AudioFile a;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Audio File:");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Audio Files .mp3, .aif, .aiff, .wav, .aac, .m4a", "*.wav", "*.mp3", "*.aac", "*.m4a"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		
		if(selectedFile == null) {
			System.exit(0);
		}
		
		String location = selectedFile.getPath();
		System.out.println(location);
		a = new AudioFile(location);
		
		primaryStage.setTitle("Quiz Game Creator: AudioFile JavaFX Player");
		Button play = new Button("Play Once");
		play.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				a.playOnce();
			}
			
		});
		Button playLoop = new Button("Play Loop");
		playLoop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				a.playLoop();
			}
			
		});
		Button stop = new Button("Stop");
		stop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				a.stop();
			}
			
		});
		Label label = new Label("Opened file: " + location);
		Button browse = new Button("Browse...");
		browse.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				a.stop();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Audio File...");
				fileChooser.getExtensionFilters().addAll(
				        new ExtensionFilter("Audio Files .mp3, .aif, .aiff, .wav, .aac, .m4a", "*.wav", "*.mp3", "*.aac", "*.m4a"),
				        new ExtensionFilter("All Files", "*.*"));
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				if(selectedFile != null)
				{
					String location = selectedFile.getPath();
					a = new AudioFile(location);
					label.setText("Opened file: " + location);
				}
			}
			
		});
		VBox.setMargin(label, new Insets(10));
		VBox root = new VBox();
		HBox hbox = new HBox();
		HBox.setMargin(play, new Insets(10));
		HBox.setMargin(playLoop, new Insets(10));
		HBox.setMargin(stop, new Insets(10));
		HBox.setMargin(browse, new Insets(10));
		hbox.getChildren().addAll(play,playLoop,stop,browse);
		root.getChildren().addAll(label,hbox);
		Scene scene = new Scene(root,450,80);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
