package com.quizcreator.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.quizcreator.tools.FolderTools;

public class Project {
	private String title;
	private Quiz quiz;
	private String author;
	private String projectLocation;
	private ArrayList<AudioFile> audioResourceList;
	private ArrayList<ImageFile> imageResourceList;
	
	/**
	 * Beim Erstellen eines neuen Projekts wird auch immer der temp. Arbeitsordner geleert.
	 * @param projectLocation
	 */
	public Project(String projectLocation) {
		FolderTools.eraseWorkFolder();
		title = "";
		author = "";
		this.projectLocation = projectLocation;
		audioResourceList = new ArrayList<AudioFile>();
		imageResourceList = new ArrayList<ImageFile>();
		quiz = new Quiz();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Quiz getQuiz() {
		return quiz;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public List<AudioFile> getAudioResourceList() {
		return audioResourceList;
	}
	public void setAudioResourceList(ArrayList<AudioFile> audioResourceList) {
		this.audioResourceList = audioResourceList;
	}

	public void deleteFromAudioRessourceList(final UUID id) {
		audioResourceList.removeIf(file -> id.equals(file.getId()));
	}

	public AudioFile getAudioFileFromAudioResourceList(String description) {
		for (final AudioFile a : audioResourceList) {
			if (description.equals(a.getDescription())) {
				return a;
			}
		}
		return null;
	}

	public AudioFile getAudioFileFromAudioResourceList(final UUID id) {
		for (final AudioFile a : audioResourceList) {
			if (id.equals(a.getId())) {
				return a;
			}
		}
		return null;
	}
	
	public List<ImageFile> getImageResourceList() {
		return imageResourceList;
	}
	public void setImageResourceList(ArrayList<ImageFile> imageResourceList) {
		this.imageResourceList = imageResourceList;
	}

	public void addToImageRessourceList(ImageFile file) {
		imageResourceList.add(file);
	}

	public void deleteFromImageRessourceList(final UUID id) {
		imageResourceList.removeIf(image -> image.getId().equals(id));
	}

	public ImageFile getImageFileFromImageResourceList(String description) {
		for (final ImageFile image : imageResourceList) {
			if (description.equals(image.getDescription())) {
				return image;
			}
		}
		return null;
	}

	public ImageFile getImageFileFromImageResourceList(UUID id) {
		for (final ImageFile image : imageResourceList) {
			if (id.equals(image.getId())) {
				return image;
			}
		}
		return null;
	}
}
