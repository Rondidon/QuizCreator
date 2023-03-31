package com.quizcreator.app.data;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class QuizDesign {
	private ImageFile titleIcon;
	private String titleText;
	private String startScreenTitle;
	private String startScreenSubTitle;
	private String startScreenText;
	private String startScreenAuthor;
	private ImageFile startScreenLogo;
	private Color startScreenBackgroundColor;
	private Color startScreenTextColor;
	private Font startScreenFont;
	private Font quizFont;
	private Color quizBackgroundColor;
	private Color quizQuestionBackgroundColor;
	private Color quizQuestionTextColor;
	private Color quizAnswerBackgroundColor;
	private Color quizAnswerTextColor;
	
	public QuizDesign() {
		setTitleIcon(null);
		setTitleText("");
		setStartScreenTitle("");
		setStartScreenSubTitle("");
		setStartScreenText("");
		setStartScreenAuthor("");
		setStartScreenLogo(null);
		Color c = new Color(1,1,1,0);
		setStartScreenBackgroundColor(c);
		setStartScreenTextColor(c);
		setQuizBackgroundColor(c);
		setQuizQuestionBackgroundColor(c);
		setQuizQuestionTextColor(c);
		setQuizAnswerBackgroundColor(c);
		setQuizAnswerTextColor(c);
		Font f = Font.getDefault();
		setStartScreenFont(f);
		setQuizFont(f);
	}
	
	public ImageFile getTitleIcon() {
		return titleIcon;
	}
	public void setTitleIcon(ImageFile titleIcon) {
		this.titleIcon = titleIcon;
	}
	public String getTitleText() {
		return titleText;
	}
	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}
	public String getStartScreenTitle() {
		return startScreenTitle;
	}
	public void setStartScreenTitle(String startScreenTitle) {
		this.startScreenTitle = startScreenTitle;
	}
	public String getStartScreenSubTitle() {
		return startScreenSubTitle;
	}
	public void setStartScreenSubTitle(String startScreenSubTitle) {
		this.startScreenSubTitle = startScreenSubTitle;
	}
	public String getStartScreenText() {
		return startScreenText;
	}
	public void setStartScreenText(String startScreenText) {
		this.startScreenText = startScreenText;
	}
	public String getStartScreenAuthor() {
		return startScreenAuthor;
	}
	public void setStartScreenAuthor(String startScreenAuthor) {
		this.startScreenAuthor = startScreenAuthor;
	}
	public ImageFile getStartScreenLogo() {
		return startScreenLogo;
	}
	public void setStartScreenLogo(ImageFile startScreenLogo) {
		this.startScreenLogo = startScreenLogo;
	}
	public Color getStartScreenBackgroundColor() {
		return startScreenBackgroundColor;
	}
	public void setStartScreenBackgroundColor(Color startScreenBackgroundColor) {
		this.startScreenBackgroundColor = startScreenBackgroundColor;
	}
	public Color getStartScreenTextColor() {
		return startScreenTextColor;
	}
	public void setStartScreenTextColor(Color startScreenTextColor) {
		this.startScreenTextColor = startScreenTextColor;
	}
	public Font getStartScreenFont() {
		return startScreenFont;
	}
	public void setStartScreenFont(Font startScreenFont) {
		this.startScreenFont = startScreenFont;
	}
	public Font getQuizFont() {
		return quizFont;
	}
	public void setQuizFont(Font quizFont) {
		this.quizFont = quizFont;
	}
	public Color getQuizBackgroundColor() {
		return quizBackgroundColor;
	}
	public void setQuizBackgroundColor(Color quizBackgroundColor) {
		this.quizBackgroundColor = quizBackgroundColor;
	}
	public Color getQuizQuestionBackgroundColor() {
		return quizQuestionBackgroundColor;
	}
	public void setQuizQuestionBackgroundColor(Color quizQuestionBackgroundColor) {
		this.quizQuestionBackgroundColor = quizQuestionBackgroundColor;
	}
	public Color getQuizQuestionTextColor() {
		return quizQuestionTextColor;
	}
	public void setQuizQuestionTextColor(Color quizQuestionTextColor) {
		this.quizQuestionTextColor = quizQuestionTextColor;
	}
	public Color getQuizAnswerBackgroundColor() {
		return quizAnswerBackgroundColor;
	}
	public void setQuizAnswerBackgroundColor(Color quizAnswerBackgroundColor) {
		this.quizAnswerBackgroundColor = quizAnswerBackgroundColor;
	}
	public Color getQuizAnswerTextColor() {
		return quizAnswerTextColor;
	}
	public void setQuizAnswerTextColor(Color quizAnswerTextColor) {
		this.quizAnswerTextColor = quizAnswerTextColor;
	}
}