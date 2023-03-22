package com.quizcreator.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QuizCreatorController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}