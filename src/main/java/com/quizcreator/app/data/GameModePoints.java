package com.quizcreator.app.data;

public class GameModePoints extends GameMode {
	
	private boolean showTime;
	
	public GameModePoints() {
		super();
		setShowTime(false);
	}
	
	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}
	
	public boolean isShowTime() {
		return showTime;
	}
}
