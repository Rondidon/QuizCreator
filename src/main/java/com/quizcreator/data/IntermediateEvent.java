package com.quizcreator.data;

import java.util.UUID;

public class IntermediateEvent extends Event {
	private IntermediateEventType type = IntermediateEventType.SHOW_AMOUNT_OF_ANSWERED_QUESTIONS;
	private String text = "";
	private AudioFile audio;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 */
	public IntermediateEvent() {
		super();
		if(Program.DEBUG) System.out.println("New QuestionContainer registered: Id: " + getId());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 */
	public IntermediateEvent(final UUID id) {
		super(id);
		setText("");
		if(Program.DEBUG) System.out.println("New IntermediateEvent registered: Id: " + getId());
	}
	
	public IntermediateEventType getType() {
		return type;
	}
	
	public void setType(final IntermediateEventType type) {
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(final String text) {
		this.text = text;
	}
	
	public AudioFile getAudio() {
		return audio;
	}
	
	public void setAudio(final AudioFile audio) {
		this.audio = audio;
	}
}
