package com.quizcreator.data;

import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Stage;

public class Program extends Application {
	private static String version = "Development 05-2020";
	private static Project project;
	private static HashMap<String,String> projectLocations = new HashMap<String,String>();
	public static final boolean DEBUG = false; // Debug mode - turn on for Console outputs for debugging
	
	/**
	 * configures the program
	 */
	public static void setupProgram() {
		if(Program.DEBUG) System.out.println("Version: " + version + ".");
		project = new Project("");
	}
	
	public static Project getProject() {
		return project;
	}
	public static void setProject(Project p) {
		project = p;
	}
	public static void setVersion(String v) {
		version = v;
	}
	public static String getVersion() {
		return version;
	}

	public static HashMap<String,String> getProjectLocations() {
		return projectLocations;
	}

	public static void setProjectLocations(HashMap<String,String> projectLocations) {
		Program.projectLocations = projectLocations;
	}

	/**
	 * Main function / entry point
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Starts the Java FX framework
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		WindowManager.addStage(primaryStage, "welcomeStage");
		WindowManager.openWelcomeStage(primaryStage,true);
	}
	
}
