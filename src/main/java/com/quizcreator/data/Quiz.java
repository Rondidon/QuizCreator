package com.quizcreator.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {
	private ArrayList<Event> eventList;
	private QuizDesign quizDesign;
	private ArrayList<Player> playerList;
	private Countdown countdown;
	private GameMode gameMode;
	private Event actualEvent;
	private int multiplayerMode;
	private SoundSettings soundSettings;
	
	public Quiz() {
		eventList = new ArrayList<Event>();
		quizDesign = new QuizDesign();
		playerList = new ArrayList<Player>();
		countdown = new Countdown();
		gameMode = new GameModePoints();
		actualEvent = null;
		multiplayerMode = 0;
		soundSettings = new SoundSettings();
	}
	
	public List<Event> getEventList() {
		return eventList;
	}
	
	public void setEventList(ArrayList<Event> eventList) {
		this.eventList = eventList;
	}
	
	public List<QuestionContainer> getQuestionContainerList() {
		List<QuestionContainer> list = new ArrayList<QuestionContainer>();
		for(Event e : this.eventList) {
			if(e instanceof QuestionContainer) {
				list.add((QuestionContainer) e); 
			}
		}
		return list;
	}
	
	public void updateQuestionAmount() {
		for(Event e : this.eventList) {
			if(e instanceof QuestionContainer) {
				((QuestionContainer) e).setQuestionAmount(((QuestionContainer) e).getQuestionList().size());
			}
		}
	}
	
	/**
	 * Swaps two Elements on the eventList. Index starts at zero.
	 * @param index1
	 * @param index2
	 */
	public void swapEntriesOnEventList(int index1, int index2) {
		Collections.swap(eventList, index1, index2);
	}
	/**
	 * Adds an element at the end of the Eventlist.
	 * @param e
	 */
	public void addToEventList(Event e) {
		eventList.add(e);
	}
	public void removeFromEventList(Event e) {
		eventList.remove(e);
	}
	public boolean checkIfQuestionContainerExists(String name) {
		for(QuestionContainer qc:this.getQuestionContainerList()) {
			if(qc.getDescription().equals(name)) {
				return true;
			}
		}
		return false;
	}
	public QuizDesign getQuizDesign() {
		return quizDesign;
	}
	public void setQuizDesign(QuizDesign quizDesign) {
		this.quizDesign = quizDesign;
	}
	public List<Player> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
	public void addToPlayerList(Player player) {
		playerList.add(player);
	}
	public void removeFromPlayerList(Player p) {
		playerList.remove(p);
	}
	public Countdown getCountdown() {
		return countdown;
	}
	public void setCountdown(Countdown countdown) {
		this.countdown = countdown;
	}
	public GameMode getGameMode() {
		return gameMode;
	}
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	public Event getActualEvent() {
		return actualEvent;
	}
	public void setActualEvent(Event actualEvent) {
		this.actualEvent = actualEvent;
	}
	public int getMultiplayerMode() {
		return multiplayerMode;
	}
	public void setMultiplayerMode(int multiplayerMode) {
		this.multiplayerMode = multiplayerMode;
	}
	public SoundSettings getSoundSettings() {
		return soundSettings;
	}
	public void setSoundSettings(SoundSettings soundSettings) {
		this.soundSettings = soundSettings;
	}
	
}
