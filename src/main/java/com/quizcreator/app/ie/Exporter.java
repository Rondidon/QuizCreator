package com.quizcreator.app.ie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.quizcreator.app.QuizCreatorApplication;
import com.quizcreator.app.tools.FolderTools;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.quizcreator.app.data.*;
import com.quizcreator.app.tools.ZipUtilities;

public class Exporter {

	final FolderTools folderTools = new FolderTools(); // TODO UNBEDINGT DEPENDENCY INJECTION
	
	/**
	 * Saves the data of a whole QuizMaker project into an xml file.
	 * @param project : The project to save
	 * @param location : The location where the project shall be saved.
	 */
	public void saveProject(Project project, String location) {
		
		if(QuizCreatorApplication.DEBUG) System.out.println("----------\nSaving project to: " + location + "\n----------");
		Document doc = new Document();
		
		// Create the XML document
		Element root = new Element("QuizGameMakerProject");
		// write data into the XML file from the Java Class files:
		// Program
		exportProgramData(root);
		// Project
		exportProjectData(root, project);
		// QuizDesign
		exportQuizDesign(root, project);
		// SoundSettings
		exportSoundSettings(root, project);
		// Timer
		exportCountdown(root, project);
		// GameMode
		exportGameMode(root, project);
		// Quiz
		exportQuiz(root, project);
		
		doc.setRootElement(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		try {	
			// Betriebssystemabhaengiger Arbeitsordner im Home-Verzeichnis, 
			// wo in einem Unterordner die ganze Verzeichnisstruktur
			// der spaeteren QGM Datei aufgebaut wird.
			String workFolder = folderTools.getTempFolderLocation();
			// Ort der XML Projekt-Datei
			String xmlLocation = workFolder.concat("/project.xml");
			
			// Falls Projektordner nicht bereits existiert, dann erstellen
			File f = new File(workFolder);
			if(!f.exists()) {
				f.mkdirs();
			}
			
			if(QuizCreatorApplication.DEBUG) {
				System.out.println("XML Outputter Location: " + xmlLocation);
				System.out.println("Project QGM-File Location: " + location);
			}

			saveProgramSettings();
			
			// oben aufgebautes doc-Dokument als XML Datei an der xmlLocation speichern 
			FileOutputStream xmloutput =  new FileOutputStream(new File(xmlLocation));
			outputter.output(doc, xmloutput);
			xmloutput.close();
			
			// Den Arbeitsordner zippen und an Stelle der location ausgeben. Existierendes überschreiben. 
	        ZipUtilities.folderToZIP(workFolder, location, workFolder, true);
			// Project location refreshen. QGM Datei ist jetzt wo anders.
			project.setProjectLocation(location);
			if(QuizCreatorApplication.DEBUG) System.out.println("----------\nThe project has been saved successfully :-)\n----------");
		} catch (IOException e) {
			System.out.println("----------\nAn error occurred while saving the project :-(\n----------");
			e.printStackTrace();
		}
	}
	
	public void exportQuestionContainer(String location, List<QuestionContainer> list) {
		// TODO
	}
	
	/**
	 * Saves the data of the Program class into the XML save file
	 * @param root
	 */
	private void exportProgramData(Element root) {
		root.setAttribute("programVersion", QuizCreatorApplication.getVersion());
		if(QuizCreatorApplication.DEBUG) System.out.println("Program data successfully saved...");
	}
	
	/**
	 * Saves the data of the Project class into the XML save file
	 * @param root
	 * @param project
	 */
	private void exportProjectData(Element root, Project project) {
		root.setAttribute("title", project.getTitle());
		root.setAttribute("author", project.getAuthor());
		Element audioResList = new Element("audioResourceList");
		Iterator<AudioFile> it = project.getAudioResourceList().iterator();
		while(it.hasNext()) {
			AudioFile a = it.next();
			Element audioFile = new Element("AudioFile");
			audioFile.setAttribute("id", a.getId().toString());
			String l = a.getLocation();
			audioFile.setAttribute("location", l.substring(l.indexOf("/audio/"), l.length()));
			audioFile.setAttribute("description", a.getDescription());
			audioFile.setAttribute("type", a.getPlaybackMode().toString());
			audioFile.setAttribute("volume", Double.toString(a.getVolume()));
			audioResList.addContent(audioFile);
		}
		Element imageResList = new Element("imageResourceList");
		Iterator<ImageFile> it2 = project.getImageResourceList().iterator();
		while(it2.hasNext()) {
			ImageFile i = it2.next();
			Element imageFile = new Element("ImageFile");
			imageFile.setAttribute("id", i.getId().toString());
			String l = i.getLocation();
			imageFile.setAttribute("location", l.substring(l.indexOf("/image/"), l.length()));
			imageFile.setAttribute("description", i.getDescription());
			imageResList.addContent(imageFile);
		}

		root.addContent(audioResList);
		root.addContent(imageResList);
		if(QuizCreatorApplication.DEBUG) System.out.println("Project data successfully saved...");
	}
	
	/**
	 * Saves the data of the QuizDesign class into the XML save file
	 * @param root
	 */
	private void exportQuizDesign(Element root, Project project) {
		// Quiz Design
		Element quizDesign = new Element("QuizDesign");
		QuizDesign qd = project.getQuiz().getQuizDesign();
		if(qd.getTitleIcon() != null) {
			quizDesign.setAttribute("titleIcon", qd.getTitleIcon().getId().toString());
		}
		else {
			quizDesign.setAttribute("titleIcon", "null");
		}
		quizDesign.setAttribute("titleText", qd.getTitleText());
		quizDesign.setAttribute("startScreenTitle", qd.getStartScreenTitle());
		quizDesign.setAttribute("startScreenSubTitle", qd.getStartScreenSubTitle());
		quizDesign.setAttribute("startScreenText", qd.getStartScreenText());
		quizDesign.setAttribute("startScreenAuthor", qd.getStartScreenAuthor());
		if(qd.getStartScreenLogo() != null) {
			quizDesign.setAttribute("startScreenLogo", qd.getStartScreenLogo().getId().toString());
		}
		else {
			quizDesign.setAttribute("startScreenLogo", "null");
		}
		
		Element ssBackgroundColor = new Element("startScreenBackgroundColor");
		ssBackgroundColor.setAttribute("red",Double.toString(qd.getStartScreenBackgroundColor().getRed()));
		ssBackgroundColor.setAttribute("green", Double.toString(qd.getStartScreenBackgroundColor().getGreen()));
		ssBackgroundColor.setAttribute("blue", Double.toString(qd.getStartScreenBackgroundColor().getBlue()));
		ssBackgroundColor.setAttribute("opacity", Double.toString(qd.getStartScreenBackgroundColor().getOpacity()));
		quizDesign.addContent(ssBackgroundColor);
		
		Element ssTextColor = new Element("startScreenTextColor");
		ssTextColor.setAttribute("red",Double.toString(qd.getStartScreenTextColor().getRed()));
		ssTextColor.setAttribute("green",Double.toString(qd.getStartScreenTextColor().getGreen()));
		ssTextColor.setAttribute("blue",Double.toString(qd.getStartScreenTextColor().getBlue()));
		ssTextColor.setAttribute("opacity",Double.toString(qd.getStartScreenTextColor().getOpacity()));
		quizDesign.addContent(ssTextColor);
		
		//quizBackgroundColor
		Element quizBackgroundColor = new Element("QuizBackgroundColor");
		quizBackgroundColor.setAttribute("red",Double.toString(qd.getQuizBackgroundColor().getRed()));
		quizBackgroundColor.setAttribute("green",Double.toString(qd.getQuizBackgroundColor().getGreen()));
		quizBackgroundColor.setAttribute("blue",Double.toString(qd.getQuizBackgroundColor().getBlue()));
		quizBackgroundColor.setAttribute("opacity",Double.toString(qd.getQuizBackgroundColor().getOpacity()));
		quizDesign.addContent(quizBackgroundColor);
		//quizQuestionBackgroundColor
		Element quizQuestionBackgroundColor = new Element("QuizQuestionBackgroundColor");
		quizQuestionBackgroundColor.setAttribute("red",Double.toString(qd.getQuizQuestionBackgroundColor().getRed()));
		quizQuestionBackgroundColor.setAttribute("green",Double.toString(qd.getQuizQuestionBackgroundColor().getGreen()));
		quizQuestionBackgroundColor.setAttribute("blue",Double.toString(qd.getQuizQuestionBackgroundColor().getBlue()));
		quizQuestionBackgroundColor.setAttribute("opacity",Double.toString(qd.getQuizQuestionBackgroundColor().getOpacity()));
		quizDesign.addContent(quizQuestionBackgroundColor);
		//quizQuestionTextColor
		Element quizQuestionTextColor = new Element("QuizQuestionTextColor");
		quizQuestionTextColor.setAttribute("red",Double.toString(qd.getQuizQuestionTextColor().getRed()));
		quizQuestionTextColor.setAttribute("green",Double.toString(qd.getQuizQuestionTextColor().getGreen()));
		quizQuestionTextColor.setAttribute("blue",Double.toString(qd.getQuizQuestionTextColor().getBlue()));
		quizQuestionTextColor.setAttribute("opacity",Double.toString(qd.getQuizQuestionTextColor().getOpacity()));
		quizDesign.addContent(quizQuestionTextColor);
		//quizAnswerBackgroundColor
		Element quizAnswerBackgroundColor = new Element ("QuizAnswerBackgroundColor");
		quizAnswerBackgroundColor.setAttribute("red",Double.toString(qd.getQuizAnswerBackgroundColor().getRed()));
		quizAnswerBackgroundColor.setAttribute("green",Double.toString(qd.getQuizAnswerBackgroundColor().getGreen()));
		quizAnswerBackgroundColor.setAttribute("blue",Double.toString(qd.getQuizAnswerBackgroundColor().getBlue()));
		quizAnswerBackgroundColor.setAttribute("opacity",Double.toString(qd.getQuizAnswerBackgroundColor().getOpacity()));
		quizDesign.addContent(quizAnswerBackgroundColor);
		//quizAnswerTextColor
		Element quizAnswerTextColor = new Element("QuizAnswerTextColor"); 
		quizAnswerTextColor.setAttribute("red",Double.toString(qd.getQuizAnswerTextColor().getRed()));
		quizAnswerTextColor.setAttribute("green",Double.toString(qd.getQuizAnswerTextColor().getGreen()));
		quizAnswerTextColor.setAttribute("blue",Double.toString(qd.getQuizAnswerTextColor().getBlue()));
		quizAnswerTextColor.setAttribute("opacity",Double.toString(qd.getQuizAnswerTextColor().getOpacity()));
		quizDesign.addContent(quizAnswerTextColor);
		
		Element quizFont = new Element("quizFont");
		quizFont.setAttribute("name",qd.getQuizFont().getName());
		quizFont.setAttribute("size", Double.toString(qd.getQuizFont().getSize()));
		quizDesign.addContent(quizFont);
		
		Element startScreenFont = new Element("startScreenFont");
		startScreenFont.setAttribute("name",qd.getStartScreenFont().getName());
		startScreenFont.setAttribute("size", Double.toString(qd.getStartScreenFont().getSize()));
		quizDesign.addContent(startScreenFont);
		
		root.addContent(quizDesign);
		if(QuizCreatorApplication.DEBUG) System.out.println("Quiz design successfully saved...");
	}
	
	/**
	 * Saves the data of the SoundSettings class into the XML save file
	 * @param root
	 */
	private void exportSoundSettings(Element root, Project project) {
		Element soundSettings = new Element("SoundSettings");
		SoundSettings ss = project.getQuiz().getSoundSettings();
		if(ss.getSfxAnswerRight() != null) {
			soundSettings.setAttribute("sfxAnswerRight",ss.getSfxAnswerRight().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxAnswerRight","null");
		}
		if(ss.getSfxAnswerWrong() != null) {
			soundSettings.setAttribute("sfxAnswerWrong",ss.getSfxAnswerWrong().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxAnswerWrong","null");
		}
		if(ss.getSfxAnswerLogin() != null) {
			soundSettings.setAttribute("sfxAnswerLogin",ss.getSfxAnswerLogin().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxAnswerLogin","null");
		}
		if(ss.getSfxGameWon() != null) {
			soundSettings.setAttribute("sfxGameWon",ss.getSfxGameWon().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxGameWon","null");
		}
		if(ss.getSfxGameLost() != null) {
			soundSettings.setAttribute("sfxGameLost",ss.getSfxGameLost().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxgameLost","null");
		}
		if(ss.getSfxProgramStartup() != null) {
			soundSettings.setAttribute("sfxProgramStartup",ss.getSfxProgramStartup().getId().toString());
		}
		else {
			soundSettings.setAttribute("sfxProgramStartup","null");
		}
		if(ss.getBackgroundMusic() != null) {
			soundSettings.setAttribute("backgroundMusic",ss.getBackgroundMusic().getId().toString());
		}
		else {
			soundSettings.setAttribute("backgroundMusic","null");
		}
		root.addContent(soundSettings);
		if(QuizCreatorApplication.DEBUG) System.out.println("Sound settings successfully saved...");
	}
	
	/**
	 * Saves the data of the Timer class into the XML save file
	 * @param root
	 */
	private void exportCountdown(Element root, Project project) {
		Element timer = new Element("Timer");
		timer.setAttribute("timeInSeconds", Float.toString(project.getQuiz().getCountdown().getTimeInSeconds()));
		timer.setAttribute("modeUpwards", Boolean.toString(project.getQuiz().getCountdown().isModeUpwards()));
		root.addContent(timer);
		if(QuizCreatorApplication.DEBUG) System.out.println("Timer data successfully saved...");
	}
	
	/**
	 * Saves the data of the GameMode class into the XML save file
	 * @param root
	 */
	private void exportGameMode(Element root, Project project) {
		Element gameMode = new Element("GameMode");
		GameMode gm = project.getQuiz().getGameMode();
		if(gm instanceof GameModePoints) {
			gameMode.setAttribute("type", "Points");
		}
		if(gm instanceof GameModeTime) {
			gameMode.setAttribute("type", "Time");
		}
		if(gm instanceof GameModeMillionaire) {
			gameMode.setAttribute("type", "Millionaire");
		}
		gameMode.setAttribute("tipJoker", Integer.toString(gm.getTipJoker()));
		gameMode.setAttribute("fiftyJoker", Integer.toString(gm.getFiftyJoker()));
		gameMode.setAttribute("changeJoker", Integer.toString(gm.getChangeJoker()));
		gameMode.setAttribute("feedbackMode", gm.getFeedbackMode().toString());
		if(gm instanceof GameModeMillionaire) {
			GameModeMillionaire gmm = (GameModeMillionaire)gm;
			gameMode.setAttribute("showTime", Boolean.toString(gmm.isShowTime()));
		}
		if(gm instanceof GameModePoints) {
			GameModePoints gmp = (GameModePoints)gm;
			gameMode.setAttribute("showTime", Boolean.toString(gmp.isShowTime()));
		}
		if(gm instanceof GameModeTime) {
			GameModeTime gmt = (GameModeTime)gm;
			gameMode.setAttribute("timeAtStart", Integer.toString(gmt.getTimeAtStart()));
		}
		root.addContent(gameMode);
		if(QuizCreatorApplication.DEBUG) System.out.println("Game mode successfully saved...");
	}
	
	/**
	 * Saves the data of the Quiz class into the XML save file
	 * @param root
	 */
	private void exportQuiz(Element root, Project project) {
		Element quiz = new Element("Quiz");
		quiz.setAttribute("multiplayerMode",Integer.toString(project.getQuiz().getMultiplayerMode()));
		exportEventList(quiz,project);
		root.addContent(quiz);
	}
	
	/**
	 * Saves the eventList from the Quiz class into the XML save file
	 * @param root
	 */
	private void exportEventList(Element root, Project project) {
		Element eventList = new Element("EventList");
		Iterator<Event> it = project.getQuiz().getEventList().iterator();
		while(it.hasNext()) {
			Event e = it.next();
			if(e instanceof IntermediateEvent) {
				Element intermediateEvent = new Element("IntermediateEvent");
				intermediateEvent.setAttribute("id",e.getId().toString());
				intermediateEvent.setAttribute("type",((IntermediateEvent) e).getType().toString());
				intermediateEvent.setAttribute("text",((IntermediateEvent) e).getText());
				if(((IntermediateEvent) e).getAudio() != null) {
					intermediateEvent.setAttribute("audio",((IntermediateEvent) e).getAudio().getId().toString());
				}
				else {
					intermediateEvent.setAttribute("audio","null");
				}
				eventList.addContent(intermediateEvent);
			}
			if(e instanceof QuestionContainer) {
				Element questionContainer = new Element("QuestionContainer");
				questionContainer.setAttribute("id",e.getId().toString());
				questionContainer.setAttribute("description",((QuestionContainer) e).getDescription());
				questionContainer.setAttribute("timeMinusWrongAnswer",Integer.toString(((QuestionContainer) e).getTimeMinusWrongAnswer()));
				questionContainer.setAttribute("timePlusRightAnswer",Integer.toString(((QuestionContainer) e).getTimePlusRightAnswer()));
				questionContainer.setAttribute("winAmount",Integer.toString(((QuestionContainer) e).getWinAmount()));
				questionContainer.setAttribute("points",Integer.toString(((QuestionContainer) e).getPoints()));
				questionContainer.setAttribute("addedToQuiz", Boolean.toString(((QuestionContainer) e).isAddedToQuiz()));
				exportQuestionList(questionContainer, (QuestionContainer)e);
				eventList.addContent(questionContainer);
			}
		}
		root.addContent(eventList);
		if(QuizCreatorApplication.DEBUG) System.out.println("Event list data successfully saved...");
	}
	
	/**
	 * Saves the questionList of an event into the XML save file
	 */
	private void exportQuestionList(Element questionContainer, QuestionContainer e) {
		Iterator<Question> it = e.getQuestionList().iterator();
		while(it.hasNext()) {
			Question q = it.next();
			Element question = new Element("Question"); 
			
			if(q.getQuestionType() == Question.TYPE_MULTIPLEANSWERS) {
				question.setName("QuestionMultipleAnswers");	
			}
			if(q.getQuestionType() == Question.TYPE_MULTIPLECHOICE) {
				question.setName("QuestionMultipleChoice");	
			}
			if(q.getQuestionType() == Question.TYPE_TEXTFIELD) {
				question.setName("QuestionTextField");
			}
			
			question.setAttribute("id",q.getId().toString());
			question.setAttribute("text",q.getText());
			if(QuizCreatorApplication.DEBUG) System.out.println("Saving question: " + q.getText());
			if(q.getImage() != null) {
				question.setAttribute("image",q.getImage().getId().toString());
			}
			else {
				question.setAttribute("image", "null");
			}
			if(q.getAudioAutomatic() != null) {	
				question.setAttribute("audioAutomatic",q.getAudioAutomatic().getId().toString());
			}
			else {
				question.setAttribute("audioAutomatic","null");
			}
			if(q.getAudioManual() != null) {
				question.setAttribute("audioManual",q.getAudioManual().getId().toString());
			}
			else {
				question.setAttribute("audioManual","null");
			}
			if(q.getMusic() != null) {
				question.setAttribute("music",q.getMusic().getId().toString());
			}
			else {
				question.setAttribute("music","null");
			}
			question.setAttribute("useTipJoker",Boolean.toString(q.isTipJoker()));
			question.setAttribute("useChangeJoker",Boolean.toString(q.isChangeJoker()));
			question.setAttribute("tipJokerText",q.getTipJokerText());

			exportAnswerList(question,q);
			questionContainer.addContent(question);
			if(QuizCreatorApplication.DEBUG) System.out.println("Question list successfully saved...");
		}
	}
	
	/**
	 * Saves the answerList of a question into the XML save file
	 */
	private void exportAnswerList(Element question, Question q) {
		Iterator<Answer> it = q.getAnswerList().iterator();
		while(it.hasNext()) {
			Answer a = it.next();
			Element answer = new Element("Answer");
			answer.setAttribute("id",a.getId().toString());
			answer.setAttribute("text",a.getText());
			answer.setAttribute("correct",Boolean.toString(a.isCorrect()));		
			question.addContent(answer);
			if(QuizCreatorApplication.DEBUG) System.out.println("Saving answer: " + a.getText());
		}
	}
	
	/**
	 * Saves the configuration (language) and the project locations list into a settings.xml file
	 */
	public void saveProgramSettings() {
		// Betriebssystemabhaengiger App Data Ordner im Home-Verzeichnis, 
		String appDataFolder = folderTools.getAppDataFolderLocation();
		// Ort der XML Projekt-Datei
		String xmlLocation = appDataFolder.concat("/settings.xml");
		if(QuizCreatorApplication.DEBUG) System.out.println("----------\nSaving program settings to: " + xmlLocation + "\n----------");
		
		Document doc = new Document();
		
		// Create the XML document
		Element root = new Element("QuizGameMaker");
		
		root.setAttribute("programVersion", QuizCreatorApplication.getVersion());
		if(QuizCreatorApplication.DEBUG) System.out.println("programVersion = " + QuizCreatorApplication.getVersion());

		Element settings = new Element("settings");
		
		if(Locale.getDefault() == Locale.GERMAN) {
			settings.setAttribute("language","GERMAN");
			if(QuizCreatorApplication.DEBUG) System.out.println("language = GERMAN");
		}
		else {
			settings.setAttribute("language","ENGLISH");
			if(QuizCreatorApplication.DEBUG) System.out.println("language = ENGLISH");
		}
		
		root.addContent(settings);
		
		doc.setRootElement(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		try {	
			
			// Falls AppData Ordner nicht bereits existiert, dann erstellen
			File f = new File(appDataFolder);
			if(!f.exists()) {
				f.mkdirs();
			}
			
			// oben aufgebautes doc-Dokument als XML Datei an der xmlLocation speichern 
			FileOutputStream xmloutput =  new FileOutputStream(new File(xmlLocation));
			outputter.output(doc, xmloutput);
			xmloutput.close();
			
			if(QuizCreatorApplication.DEBUG) System.out.println("----------\nThe program settings file has been saved successfully :-)\n----------");
		} catch (IOException e) {
			System.out.println("----------\nAn error occurred while saving the program settings file :-(\n----------");
			e.printStackTrace();
		}
	}
}
