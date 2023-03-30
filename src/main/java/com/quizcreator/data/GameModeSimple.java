package com.quizcreator.data;

public class GameModeSimple extends GameMode {
	private WrongAnswerBehavior wrongAnswerBehavior = WrongAnswerBehavior.GAME_OVER;
	private boolean useCountdown = false;
	
	public GameModeSimple() {
		super();
	}
	
	public WrongAnswerBehavior getWrongAnswerBehavior() {
		return wrongAnswerBehavior;
	}
	
	public void setWrongAnswerBehavior(final WrongAnswerBehavior wrongAnswerBehavior) {
		this.wrongAnswerBehavior = wrongAnswerBehavior;
	}
	
	public boolean isUseCountdown() {
		return useCountdown;
	}
	
	public void setUseCountdown(boolean useCountdown) {
		this.useCountdown = useCountdown;
	}
	
}
