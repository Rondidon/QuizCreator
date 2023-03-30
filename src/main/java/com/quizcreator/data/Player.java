package com.quizcreator.data;

import java.util.UUID;

public class Player {
	private UUID id;
	private String name = "unknown";
	private int score;
	
	public Player() {
		this.id = UUID.randomUUID();
		setScore(0);
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int value) {
		this.score = value;
	}
	public void addToScore(int value) {
		score+=value;
	}
}
