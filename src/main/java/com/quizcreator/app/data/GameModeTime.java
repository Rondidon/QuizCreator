package com.quizcreator.app.data;

public class GameModeTime extends GameMode {
	private int timeAtStart;
	
	public GameModeTime() {
		super();
		setTimeAtStart(0);
	}
	
	public int getTimeAtStart() {
		return timeAtStart;
	}
	
	public void setTimeAtStart(int timeAtStart) {
		this.timeAtStart = timeAtStart;
	}
}
