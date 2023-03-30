package com.quizcreator.data;

import java.util.UUID;

public class Answer {
	
	private UUID id;
	private String text = "";
	private boolean correct = false;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 */
	public Answer() {
		setId(UUID.randomUUID());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 */
	public Answer(final UUID id) {
		setId(id);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}

