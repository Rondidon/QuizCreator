package com.quizcreator.app;

import com.quizcreator.app.data.Project;
import com.quizcreator.app.services.projectLocation.ProjectLocationService;
import com.quizcreator.app.services.projectLocation.ProjectLocationServiceImpl;
import com.quizcreator.app.tools.FolderTools;
import com.quizcreator.app.userinterface.SceneWelcome;
import com.quizcreator.app.userinterface.WindowManager;
import com.quizcreator.app.userinterface.images.ImageLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class QuizCreatorApplication extends Application {
    private static final Logger log = LogManager.getLogger(QuizCreatorApplication.class);
    private static String version = "Development 04-2023";
    private static Project project;

    public static final boolean DEBUG = false; // Debug mode - turn on for Console outputs for debugging

    private FolderTools folderTools;
    private ProjectLocationService projectLocationService;
    private ImageLoader imageLoader;


    /**
     * configures the program
     */
    public void setupCleanProject() {
        log.info("Version: " + version + ".");
        folderTools.eraseTempFolder();
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

    @Override
    public void start(Stage stage) throws IOException {
        folderTools = new FolderTools();
        projectLocationService = new ProjectLocationServiceImpl(folderTools);
        imageLoader = new ImageLoader();

        stage.setResizable(false);
        stage.centerOnScreen();
        WindowManager.addStage(stage, "welcomeStage");
        setupCleanProject();
        SceneWelcome.open(stage,imageLoader, projectLocationService, folderTools);
    }

    public static void main(String[] args) {
        launch();
    }
}