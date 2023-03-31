package com.quizcreator.app.data;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
	private float timeInSeconds;
	private float timeInSecondsSave;
	private boolean modeUpwards;
	private Timer timer;
	
	public Countdown() {
		setTimeInSeconds(0);
		setModeUpwards(false);
	}
	
	public void setTimeInSeconds(float timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
		this.timeInSecondsSave = timeInSeconds;
	}
	
	public float getTimeInSeconds() {
		return this.timeInSeconds;
	}
	
	public void addToTime(float time) {
		this.timeInSeconds += timeInSeconds;
	}
	
	public void removeFromTime(float time) {
		this.timeInSeconds -= timeInSeconds;
	}
	
	public boolean isModeUpwards() {
		return modeUpwards;
	}
	
	public void setModeUpwards(boolean modeUpwards) {
		this.modeUpwards = modeUpwards;
	}
	
	public void startCountdown() {
		System.out.println("startCountdown: " + timeInSeconds);
		if(timer !=null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(modeUpwards) {
					timeInSeconds+=0.1;
					System.out.println("upwards: " + timeInSeconds);
				}
				else {
					if(timeInSeconds <= 0) {
						pauseCountdown();
					}
					else {
						timeInSeconds-=0.1;
						System.out.println("downwards: " + timeInSeconds);
					}
				}
			}
		}, 100, 100);
	}
	
	public void resetCountdown() {
		timer.cancel();
		this.timeInSeconds = timeInSecondsSave;
		System.out.println("resetted: " + timeInSeconds);
	}
	
	public void pauseCountdown() {
		timer.cancel();
		System.out.println("paused: " + timeInSeconds);
	}
}
