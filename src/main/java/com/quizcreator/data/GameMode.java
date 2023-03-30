package com.quizcreator.data;

public class GameMode {
	private int tipJoker = 0;
	private int fiftyJoker = 0;
	private int changeJoker = 0;
	private FeedbackMode feedbackMode = FeedbackMode.NEVER;
	
	public GameMode() {}
	
	public int getTipJoker() {
		return tipJoker;
	}
	
	public void setTipJoker(int tipJoker) {
		this.tipJoker = tipJoker;
	}
	
	public int getFiftyJoker() {
		return fiftyJoker;
	}
	
	public void setFiftyJoker(int fiftyJoker) {
		this.fiftyJoker = fiftyJoker;
	}
	
	public int getChangeJoker() {
		return changeJoker;
	}
	
	public void setChangeJoker(int changeJoker) {
		this.changeJoker = changeJoker;
	}
	
	public FeedbackMode getFeedbackMode() {
		return feedbackMode;
	}
	
	public void setFeedbackMode(FeedbackMode feedbackMode) {
		this.feedbackMode = feedbackMode;
	}
}