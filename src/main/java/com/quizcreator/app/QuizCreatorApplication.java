package com.quizcreator.app;

import com.quizcreator.app.userinterface.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizCreatorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(QuizCreatorApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/

        stage.setResizable(false);
        stage.centerOnScreen();
        WindowManager.addStage(stage, "welcomeStage");
        WindowManager.openWelcomeStage(stage,true);
    }

    public static void main(String[] args) {
        launch();
    }
}