package de.quizgamemaker.ie;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.quizgamemaker.data.Answer;
import de.quizgamemaker.data.AudioFile;
import de.quizgamemaker.data.GameModeMillionaire;
import de.quizgamemaker.data.GameModePoints;
import de.quizgamemaker.data.GameModeTime;
import de.quizgamemaker.data.ImageFile;
import de.quizgamemaker.data.IntermediateEvent;
import de.quizgamemaker.data.Program;
import de.quizgamemaker.data.Project;
import de.quizgamemaker.data.Question;
import de.quizgamemaker.data.QuestionContainer;
import de.quizgamemaker.data.QuizDesign;
import de.quizgamemaker.data.SoundSettings;
import de.quizgamemaker.utils.FolderFinder;
import de.quizgamemaker.utils.FolderTools;
import de.quizgamemaker.utils.IDCalculator;
import de.quizgamemaker.utils.ZipUtilities;

public class Importer {
	
	private Project project;
	
	/**
	 * Loads a QGM File at a specified location, transfers the project.xml to Java internal structures
	 * and copies the resource files to the working folder 
	 * @param location
	 * @throws IncompatibleVersionException If program version not equal to qgm file version
	 */
	public void loadProject(String location) throws IncompatibleVersionException {
		
		if(Program.DEBUG) System.out.println("----------\nLoading project: " + location + "\n----------");
		// reset ID Calculator
		IDCalculator.clearIdList();
		// get working folder
		String workFolder = FolderFinder.getWorkFolderLocation();
		// Clean up working folder and make new project
		project = new Project("");
		// Unzip QGM file to the working folder
		ZipUtilities.zipToFolder(new File(location), new File(workFolder));
		// Open project.xml
		File xmlFile = new File(workFolder.concat("/project.xml"));
		SAXBuilder builder = new SAXBuilder();
		
		try {
			// XML to Java-readable Document by JDom SAXBuilder
			Document doc = builder.build(xmlFile);
			if(Program.DEBUG) System.out.println("project.xml successfully opened. Now reading content:");
			// Get the root element
			Element root = doc.getRootElement();
			
			// Parse the XML file and transfer it into internal structures
			
			// check if the program version of the XML is the same as the 
			// current program. If not, throw UnequalVersionException!
			checkProgramVersion(root);
			// Project
			importProjectData(root, project);
			// QuizDesign
			importQuizDesign(root, project);
			// SoundSettings
			importSoundSettings(root, project);
			// Timer
			importCountdown(root, project);
			// GameMode
			importGameMode(root, project);
			// Quiz
			importQuiz(root, project);
			// eventList
			importEventList(root, project);
			
			project.setProjectLocation(location);
			Program.setProject(project);
			
			// add project to last edited projects list
			Program.getProjectLocations().put(location, project.getTitle());
			Exporter exporter = new Exporter();
			exporter.saveProgramSettings();
			
			if(Program.DEBUG) System.out.println("----------\nProject loaded successfully :-)\n----------");
			
		} catch (IOException io) {
			System.out.println("----------\nCrash in loadProject IOException :-(\n----------" + io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println("----------\nCrash in loadProject JDOMException :-(\n----------" + jdomex.getMessage());
		}
	}
	
	private void checkProgramVersion(Element root) throws IncompatibleVersionException {
		if(!root.getAttributeValue("programVersion").equals(Program.getVersion())) {
			System.out.println("Program version check unsuccessful!\nThe loaded file has been made or edited with another Version of Easy Quiz Creator!");
			//throw new IncompatibleVersionException();
		}
		if(Program.DEBUG) System.out.println("Program version check successful...");
	}
	
	private void importProjectData(Element root, Project project) {
		project.setTitle(root.getAttributeValue("title"));
		project.setAuthor(root.getAttributeValue("author"));
		// Audio Resource List
		if(root.getChild("audioResourceList") != null) {
			List<Element> audioResList = root.getChild("audioResourceList").getChildren();
			for(Element e : audioResList) {
				int id = Integer.parseInt(e.getAttributeValue("id"));
				AudioFile a = new AudioFile(FolderFinder.getWorkFolderLocation().concat(e.getAttributeValue("location")), id);
				a.setDescription(e.getAttributeValue("description"));
				a.setType(Integer.parseInt(e.getAttributeValue("type")));
				a.setVolume(Double.parseDouble(e.getAttributeValue("volume")));
				project.addToAudioRessourceList(a);
				if(Program.DEBUG) System.out.println("AudioFile imported: " + a.getDescription());
			}
		}
		// Image Resource List
		if(root.getChild("imageResourceList") != null) {
			List<Element> imageResList = root.getChild("imageResourceList").getChildren();
			for(Element e : imageResList) {
				int id = Integer.parseInt(e.getAttributeValue("id"));
				ImageFile i = new ImageFile(FolderFinder.getWorkFolderLocation().concat(e.getAttributeValue("location")), id);
				i.setDescription(e.getAttributeValue("description"));
				project.addToImageRessourceList(i);
				if(Program.DEBUG) System.out.println("ImageFile imported: " +  i.getDescription());
			}
		}
		if(Program.DEBUG) System.out.println("Project data successfully loaded...");
	}
	
	private void importQuizDesign(Element root, Project project) {
		if(root.getChild("QuizDesign") != null) {
			Element e = root.getChild("QuizDesign");
			QuizDesign qd = new QuizDesign();
			
			// titleIcon
			if(e.getAttributeValue("titleIcon") != null) {
				ImageFile image;
				try {
					image = project.getImageFileFromImageResourceList(Integer.parseInt(e.getAttributeValue("titleIcon"))); 
				} catch (Exception ex) {
					image = null;
				}
				qd.setTitleIcon(image);
			}
			// startScreenTitle
			if(e.getAttribute("titleText") != null) {
				qd.setTitleText(e.getAttributeValue("titleText"));
			}
			// startScreenTitle
			if(e.getAttribute("startScreenTitle") != null) {
				qd.setStartScreenTitle(e.getAttributeValue("startScreenTitle"));
			}
			// startScreenSubTitle
			if(e.getAttribute("startScreenSubTitle") != null) {
				qd.setStartScreenSubTitle(e.getAttributeValue("startScreenSubTitle"));
			}
			// startScreenText
			if(e.getAttribute("startScreenText") != null) {
				qd.setStartScreenText(e.getAttributeValue("startScreenText"));
			}
			// startScreenAuthor
			if(e.getAttribute("startScreenAuthor") != null) {
				qd.setStartScreenAuthor(e.getAttributeValue("startScreenAuthor"));
			}
			// startScreenLogo
			if(e.getAttribute("startScreenLogo") != null) {
				ImageFile image;
				try {
					image = project.getImageFileFromImageResourceList(Integer.parseInt(e.getAttributeValue("startScreenLogo"))); 
				}
				catch(Exception ex) {
					image = null;
				}
				qd.setStartScreenLogo(image);
			}
			// startScreenBackgroundColor
			if(e.getChild("startScreenBackgroundColor") != null) {
				double red = Double.parseDouble(e.getChild("startScreenBackgroundColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("startScreenBackgroundColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("startScreenBackgroundColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("startScreenBackgroundColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setStartScreenBackgroundColor(color);
			}
			// startScreenTextColor
			if(e.getChild("startScreenTextColor") != null) {
				double red = Double.parseDouble(e.getChild("startScreenTextColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("startScreenTextColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("startScreenTextColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("startScreenTextColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setStartScreenTextColor(color);
			}
			// quizBackgroundColor
			if(e.getChild("QuizBackgroundColor") != null) {
				double red = Double.parseDouble(e.getChild("QuizBackgroundColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("QuizBackgroundColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("QuizBackgroundColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("QuizBackgroundColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setQuizBackgroundColor(color);
			}
			// quizQuestionBackgroundColor
			if(e.getChild("QuizQuestionBackgroundColor") != null) {
				double red = Double.parseDouble(e.getChild("QuizQuestionBackgroundColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("QuizQuestionBackgroundColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("QuizQuestionBackgroundColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("QuizQuestionBackgroundColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setQuizQuestionBackgroundColor(color);
			}
			// quizAnswerBackgroundColor
			if(e.getChild("QuizAnswerBackgroundColor") != null) {
				double red = Double.parseDouble(e.getChild("QuizAnswerBackgroundColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("QuizAnswerBackgroundColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("QuizAnswerBackgroundColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("QuizAnswerBackgroundColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setQuizAnswerBackgroundColor(color);
			}
			// QuizAnswerTextColor
			if(e.getChild("QuizAnswerTextColor") != null) {
				double red = Double.parseDouble(e.getChild("QuizAnswerTextColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("QuizAnswerTextColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("QuizAnswerTextColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("QuizAnswerTextColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setQuizAnswerTextColor(color);
			}
			// QuizQuestionTextColor
			if(e.getChild("QuizQuestionTextColor") != null) {
				double red = Double.parseDouble(e.getChild("QuizQuestionTextColor").getAttributeValue("red"));
				double green = Double.parseDouble(e.getChild("QuizQuestionTextColor").getAttributeValue("green"));;
				double blue = Double.parseDouble(e.getChild("QuizQuestionTextColor").getAttributeValue("blue"));
				double opacity = Double.parseDouble(e.getChild("QuizQuestionTextColor").getAttributeValue("opacity"));
				Color color = new Color(red,green,blue,opacity);
				qd.setQuizQuestionTextColor(color);
			}
			// quizFont
			if(e.getChild("quizFont") != null) {
				String name = e.getChild("quizFont").getAttributeValue("name");
				double size;
				try {
					size = Double.parseDouble(e.getChild("quizFont").getAttributeValue("size")); 
				}
				catch (Exception ex) {
					size = 12;
				}
				Font f = new Font(name, size);
				qd.setQuizFont(f);
			}
			// startScreenFont
			if(e.getChild("startScreenFont") != null) {
				String name = e.getChild("startScreenFont").getAttributeValue("name");
				if(Program.DEBUG) System.out.println(name);
				double size;
				try {
					size = Double.parseDouble(e.getChild("startScreenFont").getAttributeValue("size")); 
				}
				catch (Exception ex) {
					size = 12;
				}
				Font f = new Font(name, size);
				qd.setStartScreenFont(f);
			}
			project.getQuiz().setQuizDesign(qd);
			if(Program.DEBUG) System.out.println("Quiz design successfully loaded...");
		}
	}
	
	private void importSoundSettings(Element root, Project project) {
		if(root.getChild("SoundSettings") != null);
		{
			SoundSettings ss = new SoundSettings();
			Element e = root.getChild("SoundSettings");
			// sfxAnswerRight
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxAnswerRight")));
				ss.setSfxAnswerRight(a);
			}
			catch (Exception ex) { }
			// sfxAnswerWrong
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxAnswerWrong")));
				ss.setSfxAnswerWrong(a);
			}
			catch (Exception ex) { }
			// sfxAnswerLogin
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxAnswerLogin")));
				ss.setSfxAnswerLogin(a);
			}
			catch (Exception ex) { }
			// sfxGameWon
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxGameWon")));
				ss.setSfxGameWon(a);
			}
			catch (Exception ex) { }
			// sfxGameLost
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxGameLost")));
				ss.setSfxGameLost(a);
			}
			catch (Exception ex) { }
			// sfxProgramStartup
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("sfxProgramStartup")));
				ss.setSfxProgramStartup(a);
			}
			catch (Exception ex) { }
			// backgroundMusic
			try {
				AudioFile a = project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("backgroundMusic")));
				ss.setBackgroundMusic(a);
			}
			catch (Exception ex) { }
			
			project.getQuiz().setSoundSettings(ss);
			if(Program.DEBUG) System.out.println("Sound settings successfully loaded...");
		}
	}
	
	private void importCountdown(Element root, Project project) {
		if(root.getChild("Timer") != null) {
			Element e = root.getChild("Timer");
			try {
				project.getQuiz().getCountdown().setTimeInSeconds(Float.parseFloat(e.getAttributeValue("timeInSeconds")));
			}
			catch (Exception ex) { System.out.println("ERROR: parseFloat failed in importCountdown!"); }
			try {
				project.getQuiz().getCountdown().setModeUpwards(Boolean.parseBoolean(e.getAttributeValue("modeUpwards")));
			}
			catch (Exception ex) { System.out.println("ERROR: parseBoolean failed in importCountdown!"); }
			if(Program.DEBUG) System.out.println("Timer data successfully loaded...");
		}
	}
	
	/**
	 * Imports the Game Mode from the XML file.
	 * @param root
	 * @param project
	 */
	private void importGameMode(Element root, Project project) {
		if(root.getChild("GameMode") != null) {
			Element e = root.getChild("GameMode");
			// Game Mode Points
			if(e.getAttributeValue("type").equals("Points")) {
				// type definition
				GameModePoints gmp = new GameModePoints();
				// showTime
				try {
					gmp.setShowTime(Boolean.parseBoolean(e.getAttributeValue("showTime")));
				} catch (Exception ex) { System.out.println("ERROR: parseBoolean failed in importGameMode - showTime!"); }
				// tipJoker
				try {
					gmp.setTipJoker(Integer.parseInt(e.getAttributeValue("tipJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - tipJoker!"); }
				// fiftyJoker
				try {
					gmp.setFiftyJoker(Integer.parseInt(e.getAttributeValue("fiftyJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - fiftyJoker!"); }
				// changeJoker
				try {
					gmp.setChangeJoker(Integer.parseInt(e.getAttributeValue("changeJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - changeJoker!"); }
				// feedbackMode
				try {
					gmp.setFeedbackMode(Integer.parseInt(e.getAttributeValue("feedbackMode")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - feedbackMode!"); }
				project.getQuiz().setGameMode(gmp);
			}
			// Game Mode Time
			else if(e.getAttributeValue("type").equals("Time")) {
				// type definition
				GameModeTime gmt = new GameModeTime();
				// timeAtStart
				try {
					gmt.setTimeAtStart(Integer.parseInt(e.getAttributeValue("showTime")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - timeAtStart!"); }
				// tipJoker
				try {
					gmt.setTipJoker(Integer.parseInt(e.getAttributeValue("tipJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - tipJoker!"); }
				// fiftyJoker
				try {
					gmt.setFiftyJoker(Integer.parseInt(e.getAttributeValue("fiftyJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - fiftyJoker!"); }
				// changeJoker
				try {
					gmt.setChangeJoker(Integer.parseInt(e.getAttributeValue("changeJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - changeJoker!"); }
				// feedbackMode
				try {
					gmt.setFeedbackMode(Integer.parseInt(e.getAttributeValue("feedbackMode")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - feedbackMode!"); }
				project.getQuiz().setGameMode(gmt);
			}
			// Game Mode Millionaire
			else {
				// type definition
				GameModeMillionaire gmm = new GameModeMillionaire();
				// showTime
				try {
					gmm.setShowTime(Boolean.parseBoolean(e.getAttributeValue("showTime")));
				} catch (Exception ex) { System.out.println("ERROR: parseBoolean failed in importGameMode - showTime!"); }
				// tipJoker
				try {
					gmm.setTipJoker(Integer.parseInt(e.getAttributeValue("tipJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - tipJoker!"); }
				// fiftyJoker
				try {
					gmm.setFiftyJoker(Integer.parseInt(e.getAttributeValue("fiftyJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - fiftyJoker!"); }
				// changeJoker
				try {
					gmm.setChangeJoker(Integer.parseInt(e.getAttributeValue("changeJoker")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - changeJoker!"); }
				// feedbackMode
				try {
					gmm.setFeedbackMode(Integer.parseInt(e.getAttributeValue("feedbackMode")));
				} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importGameMode - feedbackMode!"); }
				project.getQuiz().setGameMode(gmm);
			}
			if(Program.DEBUG) System.out.println("GameMode data successfully loaded...");
		}
		else {
			System.out.println("ERROR: No GameMode data in the XML file! Trying to continue...");
		}
	}
	
	private void importQuiz(Element root, Project project) {
		try {
			project.getQuiz().setMultiplayerMode(Integer.parseInt(root.getChild("Quiz").getAttributeValue("multiplayerMode")));
			if(Program.DEBUG) System.out.println("Quiz multiplayer attributes successfully loaded...");
		} catch (Exception ex) { System.out.println("ERROR: parseInt failed in importQuiz - multiplayerMode!"); }
	}
	
	private void importEventList(Element root, Project project) {
		try {
			List<Element> list = root.getChild("Quiz").getChild("EventList").getChildren();
			for(Element e : list) {
				// Intermediate Event
				if(e.getName().equals("IntermediateEvent")) {
					int id = Integer.parseInt(e.getAttributeValue("id"));
					IntermediateEvent ie = new IntermediateEvent(id);
					ie.setType(Integer.parseInt(e.getAttributeValue("type")));
					ie.setText(e.getAttributeValue("text"));
					try {
						ie.setAudio(project.getAudioFileFromAudioResourceList(Integer.parseInt(e.getAttributeValue("audio"))));
					} catch (Exception ex) { }
					
					project.getQuiz().addToEventList(ie);
				}
				// QuestionContainer
				if(e.getName().equals("QuestionContainer")) {
					int id = Integer.parseInt(e.getAttributeValue("id"));
					QuestionContainer qc = new QuestionContainer(id);
					qc.setDescription(e.getAttributeValue("description"));
					qc.setTimeMinusWrongAnswer(Integer.parseInt(e.getAttributeValue("timeMinusWrongAnswer")));
					qc.setTimePlusRightAnswer(Integer.parseInt(e.getAttributeValue("timePlusRightAnswer")));
					qc.setWinAmount(Integer.parseInt(e.getAttributeValue("winAmount")));
					qc.setPoints(Integer.parseInt(e.getAttributeValue("points")));
					qc.setAddedToQuiz((Boolean.parseBoolean(e.getAttributeValue("addedToQuiz"))));
					qc.setQuestionList(importQuestionList(e));
					project.getQuiz().addToEventList(qc);
				}
			}
			if(Program.DEBUG) System.out.println("EventList data successfully loaded...");
		} catch (Exception e) { System.out.println("ERROR: No eventList found in the project.xml -> Exception in importEventList!"); }
		
		
	}
	
	private List<Question> importQuestionList(Element questionContainer) {
		List<Element> questionElementList = questionContainer.getChildren();
		List<Question> questionList = new LinkedList<Question>();
		// for every question
		for(Element q : questionElementList) {
			// define the type of the question
			int id = Integer.parseInt(q.getAttributeValue("id"));
			
			Question question = new Question(id);
			// define the text
			question.setText(q.getAttributeValue("text"));
			// define pointers to image and audio files
			try {
				question.setImage(project.getImageFileFromImageResourceList((Integer.parseInt(q.getAttributeValue("id")))));
			} catch (Exception e) { if(Program.DEBUG) System.out.println("DEBUG (no error): Question image is null in importQuestionList");}
			try {
				question.setAudioAutomatic(project.getAudioFileFromAudioResourceList((Integer.parseInt(q.getAttributeValue("audioAutomatic")))));
			} catch (Exception e) { if(Program.DEBUG) System.out.println("DEBUG (no error): Audio is null in importQuestionList");}
			try {
				question.setAudioManual(project.getAudioFileFromAudioResourceList((Integer.parseInt(q.getAttributeValue("audioManual")))));
			} catch (Exception e) { if(Program.DEBUG) System.out.println("DEBUG (no error): Audio is null in importQuestionList");}
			try {
				question.setMusic(project.getAudioFileFromAudioResourceList((Integer.parseInt(q.getAttributeValue("music")))));
			} catch (Exception e) { if(Program.DEBUG) System.out.println("DEBUG (no error): Music is null in importQuestionList");}
			// define options
			question.setUseTipJoker(Boolean.parseBoolean(q.getAttributeValue("useTipJoker")));
			question.setUseChangeJoker(Boolean.parseBoolean(q.getAttributeValue("useChangeJoker")));
			question.setTipJokerText(q.getAttributeValue("tipJokerText"));
			
			if(q.getName().equals("QuestionMultipleAnswers")) { question.setQuestionType(Question.TYPE_MULTIPLEANSWERS); }
			else if(q.getName().equals("QuestionMultipleChoice")) { question.setQuestionType(Question.TYPE_MULTIPLECHOICE); }
			else { question.setQuestionType(Question.TYPE_TEXTFIELD); }

			// last but not least import the answer list for each question ...
			question.setAnswerList(importAnswerList(q));
				
			// ... and register the question in the questionList
			questionList.add(question);
			}
		// return the question list
		if(Program.DEBUG) System.out.println("QuestionList data successfully loaded...");
		return questionList;
	}
	
	private List<Answer> importAnswerList(Element question) {
		List<Element> answerElementList = question.getChildren();
		List<Answer> answerList = new LinkedList<Answer>();
		for(Element e : answerElementList) {
			int id = Integer.parseInt(e.getAttributeValue("id"));
			Answer answer = new Answer(id);
			answer.setText(e.getAttributeValue("text"));
			answer.setCorrect(Boolean.parseBoolean(e.getAttributeValue("correct")));
			answerList.add(answer);
		}
		return answerList;
		// TODO
	}
	
	/**
	 * Questin container importer
	 * @param location
	 */
	public void importQuestionContainer(String location) {
		// TODO
	}
	
	/**
	 * Imports an Image File (JPG / PNG / GIF)
	 * @param srcLocation
	 * @param description
	 */
	public void importImageFile(String srcLocation, String description) {
		ImageFile image;
		try {
			image = new ImageFile(srcLocation);
			// working folder defined in FolderFinder.getWorkFolderLocation()
			String targetFolder = FolderFinder.getWorkFolderLocation() + "/image/"+ Integer.toString(image.getId());
			if(srcLocation.toLowerCase().contains("jpg")) {
				targetFolder += ".jpg";
			}
			else if(srcLocation.toLowerCase().contains(".png")) {
				targetFolder += ".png";
			}
			else if(srcLocation.toLowerCase().contains(".gif")) {
				targetFolder += ".gif";
			}
			else {
				throw new Exception("Image file import: Type not supported. Only *.jpg, *.png, *.gif.");
			}
			if(Program.DEBUG) System.out.println("Target Folder: " + targetFolder);
			// Copy file to App`s working folder
			Path from = Paths.get(srcLocation);
			Path to = Paths.get(targetFolder);
			//overwrite existing file, if exists
		    CopyOption[] options = new CopyOption[]{
		      StandardCopyOption.REPLACE_EXISTING,
		      StandardCopyOption.COPY_ATTRIBUTES
		    }; 
		    if(!Files.exists(to)) {
		    	Files.createDirectories(to);
		    }
			Files.copy(from, to, options);
			
			// Update the image file pointer
			image.setLocation(targetFolder);
			image.setDescription(description);
			// Register the image in the audio resource list
			project.addToImageRessourceList(image);
		}
		catch (Exception e) {
			System.out.println("Image file import failed.");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Imports an Audio File (MP3/WAV/AIF/AIFF) from the source location to the working folder
	 * @param srcLocation
	 * @param description
	 */
	public void importAudioFile(String srcLocation, String description) {
		AudioFile a;
		try {
			a = new AudioFile(srcLocation);
			// working folder defined in FolderFinder.getWorkFolderLocation()
			String targetFolder = FolderFinder.getWorkFolderLocation() + "/audio/"+ Integer.toString(a.getId());
			if(srcLocation.toLowerCase().contains(".mp3")) {
				targetFolder += ".mp3";
			}
			else if(srcLocation.toLowerCase().contains(".wav")) {
				targetFolder += ".wav";
			}
			else if(srcLocation.toLowerCase().contains(".aif")) {
				targetFolder += ".aif";
			}
			else if(srcLocation.toLowerCase().contains(".aiff")) {
				targetFolder += ".aiff";
			}
			else {
				throw new Exception("Audio file import: Type not supported. Ony WAF / AIF / MP3 / AIFF.");
			}
			if(Program.DEBUG) System.out.println("Target Folder: " + targetFolder);
			// Copy file to App Temp Data Folder
			Path from = Paths.get(srcLocation);
			Path to = Paths.get(targetFolder);
			//overwrite existing file, if exists
		    CopyOption[] options = new CopyOption[]{
		      StandardCopyOption.REPLACE_EXISTING,
		      StandardCopyOption.COPY_ATTRIBUTES
		    }; 
		    if(!Files.exists(to)) {
		    	Files.createDirectories(to);
		    }
			Files.copy(from, to, options);
			
			a.setLocation(targetFolder);
			a.setDescription(description);
			project.addToAudioRessourceList(a);
		}
		catch (Exception e) {
			System.out.println("Audio file import failed." 
					+ FolderFinder.getWorkFolderLocation() + "/audio/ ?");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Loads the program configuration XML file located in the App Data Folder defined in FolderFinder.getAppDataFolderLocation()
	 * @throws IncompatibleVersionException
	 */
	public void loadProgramSettings() throws IncompatibleVersionException {
	
		// get app folder
		String appFolder = FolderFinder.getAppDataFolderLocation();
		
		// Open settings.xml
		File xmlFile = new File(appFolder.concat("/settings.xml"));
		SAXBuilder builder = new SAXBuilder();
		
		try {
			// XML to Java-readable Document by JDom SAXBuilder
			Document doc = builder.build(xmlFile);
			if(Program.DEBUG) System.out.println("settings.xml successfully opened. Now reading content...");
			// Get the root element
			Element root = doc.getRootElement();
			
			// Parse the XML file and transfer it into internal structures
			
			// check if the program version of the XML is the same as the 
			// current program. If not, throw UnequalVersionException!
			checkProgramVersion(root);
			
			String language = root.getChild("settings").getAttributeValue("language");
			if(language.equals("GERMAN")) {
				Locale.setDefault(Locale.GERMAN);
			}
			else {
				Locale.setDefault(Locale.ENGLISH);
			}
			
			List<Element> list = root.getChild("projectlocations").getChildren();
			HashMap<String,String> projectlocations = new HashMap<String,String>();
			for(Element e : list) {
				if(FolderTools.isAvailable(e.getAttributeValue("location"))) {
					projectlocations.put(e.getAttributeValue("location"), e.getAttributeValue("name"));
				}
			}
			Program.setProjectLocations(projectlocations);
			
			if(Program.DEBUG) System.out.println("----------\nSettings loaded successfully :-)\n----------");
			
		} catch (IOException io) {
			//System.out.println("----------\nCrash in loadProgramSettings IOException :-(\n----------" + io.getMessage());
			System.out.println("No settings.xml file found");
		} catch (JDOMException jdomex) {
			System.out.println("----------\nCrash in loadProgramSettings JDOMException :-(\n----------" + jdomex.getMessage());
		}
	}
}
