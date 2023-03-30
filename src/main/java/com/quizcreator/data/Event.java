package com.quizcreator.data;

import java.util.UUID;

public class Event {
	UUID id;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 */
	public Event() {
		this.id = UUID.randomUUID();
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 */
	public Event(final UUID id) {
		setId(id);
	}

	public UUID getId() {
		return id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}	
}
