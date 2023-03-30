package com.quizcreator.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class QuestionContainer extends Event {

	private List<Question> questionList;
	private SimpleStringProperty description = new SimpleStringProperty();
	private SimpleIntegerProperty timeMinusWrongAnswer = new SimpleIntegerProperty();
	private SimpleIntegerProperty timePlusRightAnswer = new SimpleIntegerProperty();
	private SimpleIntegerProperty winAmount = new SimpleIntegerProperty();
	private SimpleIntegerProperty points = new SimpleIntegerProperty();
	private SimpleIntegerProperty questionAmount = new SimpleIntegerProperty();
	private boolean addedToQuiz;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 */
	public QuestionContainer() {
		super();
		questionList = new ArrayList<Question>();
		setTimeMinusWrongAnswer(0);
		setTimePlusRightAnswer(0);
		setWinAmount(0);
		setPoints(0);
		if(Program.DEBUG) System.out.println("New QuestionContainer registered: Id: " + getId());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 */
	public QuestionContainer(final UUID id) {
		super(id);
		questionList = new ArrayList<Question>();
		setTimeMinusWrongAnswer(0);
		setTimePlusRightAnswer(0);
		setWinAmount(0);
		setPoints(0);
		if(Program.DEBUG) System.out.println("New QuestionContainer registered: Id: " + getId());
	}
	
	public List<Question> getQuestionList() {
		return questionList;
	}
	
	public void addToQuestionList(Question q) {
		questionList.add(q);
	}
	
	public void removeFromQuestionList(Question q) {
		questionList.remove(q);
	}
	
	public void refreshAnswerAmountForAllQuestions() {
		for(Question q:getQuestionList()) {
			q.refreshAnswerAmount();
			if(Program.DEBUG) System.out.println("refreshAnswerAmountForAllQuestions");
		}
	}
	
	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public int getTimeMinusWrongAnswer() {
		return timeMinusWrongAnswer.get();
	}
	
	public void setTimeMinusWrongAnswer(int timeMinusWrongAnswer) {
		this.timeMinusWrongAnswer.set(timeMinusWrongAnswer);
	}
	
	public int getTimePlusRightAnswer() {
		return timePlusRightAnswer.get();
	}
	
	public void setTimePlusRightAnswer(int timePlusRightAnswer) {
		this.timePlusRightAnswer.set(timePlusRightAnswer);
	}
	
	public int getWinAmount() {
		return winAmount.get();
	}
	
	public void setWinAmount(int winAmount) {
		this.winAmount.set(winAmount);
	}
	
	public int getPoints() {
		return points.get();
	}
	
	public void setPoints(int points) {
		this.points.set(points);
	}

	public boolean isAddedToQuiz() {
		return addedToQuiz;
	}

	public void setAddedToQuiz(boolean addedToQuiz) {
		this.addedToQuiz = addedToQuiz;
	}

	public int getQuestionAmount() {
		return questionAmount.get();
	}

	public void setQuestionAmount(int questionAmount) {
		this.questionAmount.set(questionAmount);
	}
	
}
