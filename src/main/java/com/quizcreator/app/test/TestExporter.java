package com.quizcreator.app.test;

import com.quizcreator.app.QuizCreatorApplication;
import com.quizcreator.app.data.*;
import com.quizcreator.app.ie.*;

import java.io.File;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TestExporter extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Project setup
		// QuizCreatorApplication.setupCleanProject();
		Project project = QuizCreatorApplication.getProject();
		project.setTitle("TestProjekt");
		project.setAuthor("Robin Kindler");
//		Importer importer = new Importer();
//		importer.importAudioFile("/home/robin/++test++/test3.mp3", "testAudioFile");
	//	importer.importImageFile("/home/robin/++test++/image.jpg", "testImageFile");
		Quiz quiz = new Quiz();
		project.setQuiz(quiz);
		QuizDesign quizDesign = new QuizDesign();
		quiz.setQuizDesign(quizDesign);
		Font f = new Font("Arial", 15);
		quizDesign.setQuizFont(f);
		quizDesign.setStartScreenFont(f);
		//quizDesign.setTitleIcon(i);
		quizDesign.setTitleText("Supertitel");
		quizDesign.setStartScreenTitle("StartScreenSuperTitel");
		quizDesign.setStartScreenSubTitle("Subtitel");
		quizDesign.setStartScreenText("Text");
		quizDesign.setStartScreenAuthor("Robin");
		//quizDesign.setStartScreenLogo(i);
		Color c = new Color(1,1,1,1);
		quizDesign.setStartScreenBackgroundColor(c);
		quizDesign.setStartScreenTextColor(c);
		quizDesign.setStartScreenFont(f);
		quizDesign.setQuizFont(f);
		quizDesign.setQuizBackgroundColor(c);
		quizDesign.setQuizQuestionBackgroundColor(c);
		quizDesign.setQuizQuestionTextColor(c);
		quizDesign.setQuizAnswerTextColor(c);
		quizDesign.setQuizAnswerBackgroundColor(c);
		quizDesign.setQuizAnswerTextColor(c);
		
		SoundSettings ss = new SoundSettings();
/*		AudioFile a = project.getAudioFileFromAudioResourceList("testAudioFile");
		ss.setBackgroundMusic(a);
		ss.setSfxAnswerLogin(a);
		ss.setSfxAnswerRight(a);
		ss.setSfxAnswerWrong(a);
		ss.setSfxGameLost(a);
		ss.setSfxProgramStartup(a);
		ss.setSfxGameWon(a);*/
		project.getQuiz().setSoundSettings(ss);
		
		Countdown countdown = new Countdown();
		countdown.setModeUpwards(true);
		countdown.setTimeInSeconds(20);
		project.getQuiz().setCountdown(countdown);
		
		GameModePoints gmp = new GameModePoints();
		gmp.setChangeJoker(55);
		gmp.setFeedbackMode(FeedbackMode.ALWAYS);
		gmp.setFiftyJoker(22);
		gmp.setShowTime(false);
		gmp.setTipJoker(42);
		project.getQuiz().setGameMode(gmp);
	
		IntermediateEvent ie = new IntermediateEvent();
		ie.setText("Dies ist ein IntermediateEvent");
		ie.setType(IntermediateEventType.SHOW_AMOUNT_OF_POINTS);
//		ie.setAudio(a);
		project.getQuiz().addToEventList(ie);
		
		QuestionContainer qc = new QuestionContainer();
		qc.setDescription("Dies ist ein QuestionContainer");
		qc.setTimeMinusWrongAnswer(3);
		qc.setTimePlusRightAnswer(6);
		qc.setWinAmount(1000);
		qc.setPoints(50);
		Question q = new Question();
		q.setQuestionType(Question.TYPE_MULTIPLECHOICE);
//		q.setAudioAutomatic(a);
	//	q.setAudioManual(a);
		q.setUseChangeJoker(true);
		q.setImage(project.getImageFileFromImageResourceList("testImageFile"));
//		q.setMusic(a);
		q.setText("Welche Form hat eine Banane?");
		q.setUseTipJoker(true);
		q.setTipJokerText("Banane ist nicht gerade.");
		
		Answer ans = new Answer();
		ans.setCorrect(true);
		ans.setText("Krumm");
		
		Answer ans2 = new Answer();
		ans2.setCorrect(false);
		ans2.setText("Wellenförmig");
		
		q.addToAnwserList(ans);
		q.addToAnwserList(ans2);
		
		qc.addToQuestionList(q);
		
		project.getQuiz().addToEventList(qc);
		
		// Location Chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Projekt speichern unter...");
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
}
