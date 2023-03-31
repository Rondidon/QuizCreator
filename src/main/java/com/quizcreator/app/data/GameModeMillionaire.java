package com.quizcreator.app.data;

public class GameModeMillionaire extends GameMode {
	private boolean showTime;
	
	public GameModeMillionaire() {
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
