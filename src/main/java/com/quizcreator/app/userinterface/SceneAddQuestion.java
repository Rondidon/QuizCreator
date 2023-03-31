package com.quizcreator.app.userinterface;

import com.quizcreator.app.QuizCreatorApplication;
import com.quizcreator.app.data.Answer;
import com.quizcreator.app.data.Question;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class SceneAddQuestion {
	
	private ResourceBundle bundle = com.quizcreator.app.i18n.Locales.getGUIBundle();
	private Scene scene;
	
	@FXML private ChoiceBox<String> choiceBoxType;
	@FXML private TextField textFieldQuestion;
	@FXML private Button buttonAddRightAnswer;
	@FXML private Button buttonAddWrongAnswer;
	@FXML private TextField textFieldPositiveFeedback;
	@FXML private TextField textFieldNegativeFeedback;
	@FXML private CheckBox checkBoxJokerTip;
	@FXML private CheckBox checkBoxJokerChange;
	@FXML private CheckBox checkBoxJokerFiftyFifty;
	@FXML private TextField textFieldTipText;
	@FXML private Button buttonGraphic;
	@FXML private ChoiceBox choiceBoxSoundM;
	@FXML private ChoiceBox choiceBoxSoundA;
	@FXML private Button buttonCreate;
	@FXML private Button buttonCancel;
	@FXML private Button buttonPreview;
	@FXML private VBox vBoxAnswers;
	@FXML private HBox hBoxQuestion;
	@FXML private HBox hboxTipText;
	
	private static Question actualQuestion = null;
    private ArrayList<HBox> hboxList = new ArrayList<HBox>();
    private static ArrayList<Answer> newAnswerList;
    private static ArrayList<Button> disableList = new ArrayList<Button>();
    private Background backgroundGreen = new Background(new BackgroundFill(Color.web("#ccffcc"),null,null));
    private Background backgroundRed = new Background(new BackgroundFill(Color.web("#ffcccc"),null,null));
	
	/**
	 * Handles the button actions
	 * @param e
	 */
	@FXML
	private void handleButtonAction(ActionEvent e) {
		Object source = e.getSource();
		if(source == buttonCreate) {
			createQuestionAction();
		}
		if(source == buttonCancel) {
			cancelAction();
		}
		if(source == buttonPreview) {
			previewAction();
		}
		if(source == buttonAddRightAnswer) {
			addAnswerInUI(true);
		}
		if(source == buttonAddWrongAnswer) {
			addAnswerInUI(false);
		}
	}
	
	void disableOrEnableSwapButtons() {
		for (Button b : disableList)
		{
			if (newAnswerList.size() > 1) b.setDisable(false);
			else b.setDisable(true);
		}
	}
	
	/**
	 * Adds a new Answer to the question and also adds it in the UI.
	 * @param isRight -- true if right answer, false if wrong answer
	 */
	void addAnswerInUI(boolean isRight) {
		Answer a = new Answer();
		a.setCorrect(isRight);
		addAnswerInUI(a);
		newAnswerList.add(a);
		disableOrEnableSwapButtons();
	}
	
	/**
	 * Edits an exisiting answer in the UI
	 * @param a
	 */
	void addAnswerInUI(Answer a) {
		Label label = new Label();
		// layout stuff
		label.setPrefWidth(100);
		label.setMinWidth(100);
		label.setPadding(new Insets(5, 5, 5, 5));
		
		if(a.isCorrect()) {
			label.setText(bundle.getString("label_right"));
		} else {
			label.setText(bundle.getString("label_wrong"));
		}
		
		TextField tf = new TextField();
		tf.setMaxWidth(Double.MAX_VALUE);
		tf.setText(a.getText());
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			a.setText(newValue);
		});
		
		Button up = new Button("^");
		up.prefWidth(25);
		up.prefHeight(25);
		up.setDisable(true);
		Button down = new Button("v");
		down.prefWidth(25);
		down.prefHeight(25);
		down.setDisable(true);
		
		disableList.add(up);
		disableList.add(down);
		
		Button delete = new Button("x");
		delete.prefWidth(25);
		delete.prefHeight(25);
		
		HBox hbox = new HBox();
		
		hbox.setSpacing(5);
		hbox.setFillHeight(true);
		HBox.setHgrow(tf, Priority.ALWAYS);
		hbox.setPadding(new Insets(5, 5, 5, 5));
		
		if(a.isCorrect()) {
			// light green color
			hbox.setBackground(backgroundGreen);
		} else {
			// light red color
			hbox.setBackground(backgroundRed);
		}
		
		// Delete Button Action
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(QuizCreatorApplication.DEBUG) {
					System.out.println("Removed: " + a.getText() + a.toString());
				}
				newAnswerList.remove(a);
				vBoxAnswers.getChildren().remove(hbox);
				hboxList.remove(hbox);
				disableOrEnableSwapButtons();
			}
		});
		
		up.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ObservableList<Node> workingCollection = FXCollections.observableArrayList(vBoxAnswers.getChildren());
				// Swap the entries by determining the index of the up-button within the list of answers and calculating the next logical position
				int indexOfUpButton = workingCollection.indexOf(up.getParent());
				int swapPosition;
				if(indexOfUpButton == 0) swapPosition = workingCollection.size() - 1;
				else swapPosition = workingCollection.indexOf(up.getParent()) - 1;
				if(QuizCreatorApplication.DEBUG) {
					System.out.println("Entry pos: " + indexOfUpButton);
					System.out.println("Swap pos: " + swapPosition);
				}
				Collections.swap(workingCollection, indexOfUpButton,swapPosition);
				Collections.swap(newAnswerList, indexOfUpButton, swapPosition);
				Collections.swap(hboxList, indexOfUpButton, swapPosition);
				vBoxAnswers.getChildren().setAll(workingCollection);
			}
		});
		
		down.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ObservableList<Node> workingCollection = FXCollections.observableArrayList(vBoxAnswers.getChildren());
				// Swap the entries by determining the index of the down-button within the list of answers and calculating the next logical position
				int indexOfDownButton = workingCollection.indexOf(down.getParent());
				int swapPosition;
				if(indexOfDownButton == workingCollection.size() - 1) swapPosition = 0;
				else swapPosition = workingCollection.indexOf(down.getParent()) + 1;
				if(QuizCreatorApplication.DEBUG) {
					System.out.println("Entry pos: " + indexOfDownButton);
					System.out.println("Swap pos: " + swapPosition);
				}
				Collections.swap(workingCollection, indexOfDownButton,swapPosition);
				Collections.swap(newAnswerList, indexOfDownButton, swapPosition);
				Collections.swap(hboxList, indexOfDownButton, swapPosition);
				vBoxAnswers.getChildren().setAll(workingCollection);
			}
		});
		
		hbox.getChildren().addAll(label,tf,up,down,delete);
		hboxList.add(hbox);
		
		vBoxAnswers.setSpacing(5);
		vBoxAnswers.getChildren().add(hbox);
	}
	
	/**
	 * Sets the visibiltity mode of the scene
	 * @param mode TYPE_MULTIPLEANSWERS, TYPE_MULTIPLECHOICE, TYPE_TEXTFIELD
	 */
	private void setMode(int mode) {
		 vBoxAnswers.getChildren().clear();
		 hboxList.clear();
		
		// repopulate the answers list... 
		if(mode == Question.TYPE_MULTIPLECHOICE || mode == Question.TYPE_MULTIPLEANSWERS) {
			buttonAddWrongAnswer.setVisible(true);
			// add all answers:
			for(Answer a : newAnswerList) {
				addAnswerInUI(a);
			}
		}
		if(mode == Question.TYPE_TEXTFIELD) {
			buttonAddWrongAnswer.setVisible(false);
			// only add right answers:
			//for all answers in UI ..
			ArrayList<Answer> removeList = new ArrayList<Answer>();
			for(Answer a : newAnswerList) {
				if (a.isCorrect()) { 
					addAnswerInUI(a);
				}
				else {
					removeList.add(a);
				}
			}
			newAnswerList.removeAll(removeList);
		}
		disableOrEnableSwapButtons();
	}
	
	/**
	 * Gets executed on opening the scene window
	 */
	private void initialize() {
		hBoxQuestion.setBackground(new Background(new BackgroundFill(Color.web("#cce6ff"), null, null)));
		
		// Populate Question type Choice Box and select matching entry
		choiceBoxType.setItems(FXCollections.observableArrayList(
			    bundle.getString("choicebox_multiplechoice"), bundle.getString("choicebox_multipleanswers"),
			    bundle.getString("choicebox_textfield"))
		);
		choiceBoxType.getSelectionModel().select(actualQuestion.getQuestionType());
		// Set Tooltip for Question box choice box
		Tooltip t = new Tooltip();
		t.setText(bundle.getString("tooltip_questiontype"));
		choiceBoxType.setTooltip(t);
		// Set functionality of the Question choice box
		choiceBoxType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			 @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        System.out.println(choiceBoxType.getItems().get((Integer) number2));
		        if((Integer)number2 == 0) {
		        	actualQuestion.setQuestionType(Question.TYPE_MULTIPLECHOICE);
		        	setMode(Question.TYPE_MULTIPLECHOICE);
		        }
		        else if((Integer)number2 == 1) {
		        	actualQuestion.setQuestionType(Question.TYPE_MULTIPLEANSWERS);
		        	setMode(Question.TYPE_MULTIPLEANSWERS);
		        }
		        else {
		        	actualQuestion.setQuestionType(Question.TYPE_TEXTFIELD);
		        	setMode(Question.TYPE_TEXTFIELD);
		        }
		      }
		});
		
		// Sync tip Joker text field to the check box
		
		if(actualQuestion.isTipJoker()) hboxTipText.setDisable(false);
		else hboxTipText.setDisable(true);
		
		checkBoxJokerTip.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				hboxTipText.setDisable(!newValue);
			}
		});
	}
	
	/**
	 * Setups the UI to match the data of the actual question that shall be edited
	 */
	private void syncUIToQuestionData() {
		// sync Textfields
		textFieldQuestion.setText(actualQuestion.getText());
		textFieldPositiveFeedback.setText(actualQuestion.getPositiveFeedback());
		textFieldNegativeFeedback.setText(actualQuestion.getNegativeFeedback());
		textFieldTipText.setText(actualQuestion.getTipJokerText());
		
		// Sync Checkboxes
		checkBoxJokerChange.setSelected(actualQuestion.isChangeJoker());
		checkBoxJokerFiftyFifty.setSelected(actualQuestion.isUseFiftyJoker());
		checkBoxJokerTip.setSelected(actualQuestion.isTipJoker());
		if(actualQuestion.isTipJoker()) hboxTipText.setDisable(false);
		else hboxTipText.setDisable(true);
		
		int i = 0;
		// Copy answer list to be able to modify it and cancel changes
		for (Answer a: actualQuestion.getAnswerList()) {
			newAnswerList.add(a);
			addAnswerInUI(newAnswerList.get(i));
			i++;
		}
		disableOrEnableSwapButtons();
	}
	
	/**
	 * Previews the question
	 */
	private void previewAction() {
		System.out.println("Preview Question");
	}

	
	/**
	 * Returns the actual Question
	 */
	private void createQuestionAction() {
		// Apply input
		
		// set Textfield inputs
		actualQuestion.setText(textFieldQuestion.getText());
		actualQuestion.setPositiveFeedback(textFieldPositiveFeedback.getText());
		actualQuestion.setNegativeFeedback(textFieldNegativeFeedback.getText());
		actualQuestion.setTipJokerText(textFieldTipText.getText());
		
		// set Checkboxes
		actualQuestion.setUseChangeJoker(checkBoxJokerChange.isSelected());
		actualQuestion.setUseTipJoker(checkBoxJokerTip.isSelected());
		actualQuestion.setUseFiftyJoker(checkBoxJokerFiftyFifty.isSelected());
		
		// set Answer texts
		int j = 0;
		for(HBox h : hboxList) {
			TextField tf =  (TextField) h.getChildren().get(1);
			Answer answer = newAnswerList.get(j);
			answer.setText(tf.getText());
			j++;
		}
		// set Answers
		
		if(QuizCreatorApplication.DEBUG) {
			System.out.println("Answers in createQuestionAction..: ");
			for(Answer a : newAnswerList) {
				System.out.println(a.getText());
			}
		}
		
		actualQuestion.getAnswerList().clear();
		actualQuestion.setAnswerList(newAnswerList);
		
		System.out.println("Question saved.");
		
		WindowManager.setNullEffect("editorStage");
		WindowManager.closeStage("addQuestionStage");
	}
	
	/**
	 * Returns null
	 */
	private void cancelAction() {
		actualQuestion = null;
		
		WindowManager.setNullEffect("editorStage");
		WindowManager.closeStage("addQuestionStage");
	}
	
	private Scene getScene() {
		try {
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneAddQuestion.fxml"), bundle
			);
			loader.setController(this);
			scene = new Scene(loader.load());
			
		} catch (IOException e) {
			scene = null;
			e.printStackTrace();
		}
		return scene;
	}
	
	/**
	 * Shows and initializes a "New Question" scene and returns the new question or NULL if the dialog is canceled
	 * @param stage the stage where the scene should be shown in
	 * @return question or NULL
	 */
	public static Question showScene(Stage stage) {
		actualQuestion = new Question();
		actualQuestion.setQuestionType(Question.TYPE_MULTIPLECHOICE);
		newAnswerList = new ArrayList<Answer>();
		
		SceneAddQuestion sceneAQ = new SceneAddQuestion();
		stage.setScene(sceneAQ.getScene());
		sceneAQ.initialize();
		sceneAQ.buttonCreate.setText(sceneAQ.bundle.getString("button_createquestion"));
		
		// do CancelAction when closing the window
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				sceneAQ.cancelAction();
			}
		});
		
		stage.showAndWait();
		return actualQuestion;
	}
	
	/**
	 * Shows and initializes a "Edit Question" scene and returns the modified question or NULL if the dialog is canceled
	 * @param stage the stage where the scene should be shown in
	 * @return question or NULL
	 */
	public static Question showScene(Stage stage, Question q) {
		actualQuestion = q;
		newAnswerList = new ArrayList<Answer>(); // Antworten werden losgelößt von actualQuestion neu aufgebaut, um Veränderungen canceln zu können
		
		
		SceneAddQuestion sceneAQ = new SceneAddQuestion();
		stage.setScene(sceneAQ.getScene());
		sceneAQ.initialize();
		sceneAQ.syncUIToQuestionData();
		sceneAQ.buttonCreate.setText(sceneAQ.bundle.getString("button_okay"));
		
		// do CancelAction when closing the window
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				sceneAQ.cancelAction();
			}
		});
		
		stage.showAndWait();
		return actualQuestion;
	}

}
