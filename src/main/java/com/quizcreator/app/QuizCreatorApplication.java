package com.quizcreator.app;

import com.quizcreator.app.data.Project;
import com.quizcreator.app.userinterface.SceneWelcome;
import com.quizcreator.app.userinterface.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class QuizCreatorApplication extends Application {
    private static String version = "Development 04-2023";
    private static Project project;
    private static HashMap<String,String> projectLocations = new HashMap<String,String>();
    public static final boolean DEBUG = false; // Debug mode - turn on for Console outputs for debugging

    /**
     * configures the program
     */
    public static void setupProgram() {
        if(DEBUG) System.out.println("Version: " + version + ".");
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
        QuizCreatorApplication.projectLocations = projectLocations;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.centerOnScreen();
        WindowManager.addStage(stage, "welcomeStage");
        SceneWelcome.open(stage,true);
    }

    public static void main(String[] args) {
        launch();
    }
}