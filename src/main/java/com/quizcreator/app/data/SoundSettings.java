package com.quizcreator.app.data;

public class SoundSettings {
	
	private AudioFile sfxAnswerRight;
	private AudioFile sfxAnswerWrong;
	private AudioFile sfxAnswerLogin;
	private AudioFile sfxGameWon;
	private AudioFile sfxGameLost;
	private AudioFile sfxProgramStartup;
	private AudioFile backgroundMusic;
	private boolean multipleMusic;
	
	public SoundSettings() {
		setSfxAnswerRight(null);
		setSfxAnswerWrong(null);
		setSfxAnswerLogin(null);
		setSfxGameWon(null);
		setSfxGameLost(null);
		setSfxProgramStartup(null);
		setBackgroundMusic(null);
	}
	
	public AudioFile getSfxAnswerRight() {
		return sfxAnswerRight;
	}
	
	public void setSfxAnswerRight(AudioFile sfxAnswerRight) {
		this.sfxAnswerRight = sfxAnswerRight;
	}
	
	public AudioFile getSfxAnswerWrong() {
		return sfxAnswerWrong;
	}
	
	public void setSfxAnswerWrong(AudioFile sfxAnswerWrong) {
		this.sfxAnswerWrong = sfxAnswerWrong;
	}
	
	public AudioFile getSfxAnswerLogin() {
		return sfxAnswerLogin;
	}
	
	public void setSfxAnswerLogin(AudioFile sfxAnswerLogin) {
		this.sfxAnswerLogin = sfxAnswerLogin;
	}
	
	public AudioFile getSfxGameWon() {
		return sfxGameWon;
	}
	
	public void setSfxGameWon(AudioFile sfxGameWon) {
		this.sfxGameWon = sfxGameWon;
	}
	
	public AudioFile getSfxGameLost() {
		return sfxGameLost;
	}
	
	public void setSfxGameLost(AudioFile sfxGameLost) {
		this.sfxGameLost = sfxGameLost;
	}
	public AudioFile getSfxProgramStartup() {
		return sfxProgramStartup;
	}
	public void setSfxProgramStartup(AudioFile sfxProgramStartup) {
		this.sfxProgramStartup = sfxProgramStartup;
	}
	
	public AudioFile getBackgroundMusic() {
		return backgroundMusic;
	}
	
	public void setBackgroundMusic(AudioFile backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}
	
	public boolean isMultipleMusic() {
		return multipleMusic;
	}
	
	public void setMultipleMusic(boolean multipleMusic) {
		this.multipleMusic = multipleMusic;
	}
}
