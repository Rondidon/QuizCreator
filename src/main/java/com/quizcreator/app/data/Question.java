package com.quizcreator.app.data;

import java.util.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Question {
	private UUID id;
	private SimpleStringProperty text = new SimpleStringProperty();
	private SimpleIntegerProperty answerAmount = new SimpleIntegerProperty();
	private SimpleIntegerProperty questionType = new SimpleIntegerProperty();
	private SimpleStringProperty questionTypeAsString = new SimpleStringProperty();
	private ImageFile image;
	private AudioFile audioAutomatic;
	private AudioFile audioManual;
	private AudioFile music;
	private boolean useTipJoker;
	private boolean useChangeJoker;
	private boolean useFiftyJoker;
	private String tipJokerText;
	private String positiveFeedback;
	private String negativeFeedback;
	private List<Answer> answerList;
	
	public static final int TYPE_MULTIPLECHOICE = 0;
	public static final int TYPE_MULTIPLEANSWERS = 1;
	public static final int TYPE_TEXTFIELD = 2;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 */
	public Question() {
		this.id = UUID.randomUUID();
		setText("");
		setUseTipJoker(false);
		setUseChangeJoker(false);
		setTipJokerText("");
		setImage(null);
		setAudioAutomatic(null);
		setAudioManual(null);
		setMusic(null);
		this.answerAmount.set(0);
		answerList = new ArrayList<Answer>();
		if(Program.DEBUG) System.out.println("New Question registered: Id: " + getId());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 */
	public Question(final UUID id) {
		setId(id);
		setText("");
		setUseTipJoker(false);
		setUseChangeJoker(false);
		setTipJokerText("");
		setImage(null);
		setAudioAutomatic(null);
		setAudioManual(null);
		setMusic(null);
		this.answerAmount.set(0);
		answerList = new ArrayList<Answer>();
		if(Program.DEBUG) System.out.println("New Question registered: Id: " + getId());
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getText() {
		return text.get();
	}
	
	public void setText(String txt) {
		this.text.set(txt);
	}
	
	public ImageFile getImage() {
		return image;
	}
	
	public void setImage(ImageFile image) {
		this.image = image;
	}
	
	public AudioFile getAudioAutomatic() {
		return audioAutomatic;
	}
	
	public void setAudioAutomatic(AudioFile audioAutomatic) {
		this.audioAutomatic = audioAutomatic;
	}
	
	public AudioFile getAudioManual() {
		return audioManual;
	}
	
	public void setAudioManual(AudioFile audioManual) {
		this.audioManual = audioManual;
	}
	
	public AudioFile getMusic() {
		return music;
	}
	
	public void setMusic(AudioFile music) {
		this.music = music;
	}
	
	public void setUseTipJoker(Boolean a) {
		this.useTipJoker = a;
	}
	
	public boolean isTipJoker() {
		return this.useTipJoker;
	}
	
	public void setTipJokerText(String text) {
		this.tipJokerText = text;
	}
	
	public String getTipJokerText() {
		return this.tipJokerText;
	}
	
	public void setUseChangeJoker(boolean c) {
		this.useChangeJoker = c;
	}
	
	public boolean isChangeJoker() {
		return this.useChangeJoker;
	}
	
	public void addToAnwserList(Answer a) {
		this.answerList.add(a);
	}
	
	public void deleteFromAnswerList(final UUID id) {
		answerList.removeIf(answer -> answer.getId().equals(id));
	}
	
	public List<Answer> getAnswerList() {
		return this.answerList;
	}
	
	public void setAnswerList(List<Answer> list) {
		this.answerList = list;
	}
	
	public void swapEntriesOnAnswerList(int pos1, int pos2) {
		Collections.swap(this.answerList, pos1, pos2);
	}
	
	public void refreshAnswerAmount() {
		this.answerAmount.set(getAnswerList().size());
		if(Program.DEBUG) System.out.println("Refreshing answer amount of " + this.getText() + ": " + getAnswerList().size() + " = " + this.answerAmount.get());
	}
	
	private void refreshQuestionTypeAsString() {
		ResourceBundle bundle = com.quizcreator.app.i18n.Locales.getGUIBundle();
		if (this.questionType.get() == Question.TYPE_MULTIPLECHOICE) {
			this.questionTypeAsString.set(bundle.getString("choicebox_multiplechoice"));
		}
		else if (this.questionType.get() == Question.TYPE_MULTIPLEANSWERS) {
			this.questionTypeAsString.set(bundle.getString("choicebox_multipleanswers"));
		}
		else {
			this.questionTypeAsString.set(bundle.getString("choicebox_textfield"));
		}
	}
	
	public Integer getQuestionType() {
		return questionType.get();
	}

	public void setQuestionType(Integer questionType) {
		this.questionType.set(questionType);
		refreshQuestionTypeAsString();
	}
	
	public String getQuestionTypeAsString() {
		refreshQuestionTypeAsString();
		return questionTypeAsString.get();
	}

	public void setQuestionTypeAsString(String s) {
		this.questionTypeAsString.set(s);
	}
	
	public int getAnswerAmount() {
		return answerAmount.get();
	}

	public void setAnswerAmount(int answerAmount) {
		this.answerAmount.set(answerAmount);
	}

	public String getPositiveFeedback() {
		return positiveFeedback;
	}

	public void setPositiveFeedback(String positiveFeedback) {
		this.positiveFeedback = positiveFeedback;
	}

	public String getNegativeFeedback() {
		return negativeFeedback;
	}

	public void setNegativeFeedback(String negativeFeedback) {
		this.negativeFeedback = negativeFeedback;
	}

	public boolean isUseFiftyJoker() {
		return useFiftyJoker;
	}

	public void setUseFiftyJoker(boolean useFiftyJoker) {
		this.useFiftyJoker = useFiftyJoker;
	}
}