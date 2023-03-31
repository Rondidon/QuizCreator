package com.quizcreator.app.test;

import com.quizcreator.app.data.Countdown;

public class TestCountdown {
	public static void main(String[] args) {
		Countdown c = new Countdown();
		c.setTimeInSeconds(20);
		c.startCountdown();
		try {
			Thread.sleep(5000);
			c.resetCountdown();
			Thread.sleep(3000);
			c.startCountdown();
			Thread.sleep(5000);
			c.resetCountdown();
			c.setModeUpwards(true);
			c.startCountdown();
			Thread.sleep(5000);
			c.setModeUpwards(false);
			c.startCountdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
