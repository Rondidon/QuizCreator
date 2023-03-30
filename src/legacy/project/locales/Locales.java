package de.quizgamemaker.locales;

import java.util.Locale;
import java.util.ResourceBundle;

public class Locales {
	private static ResourceBundle languageBundle = ResourceBundle.getBundle("de.quizgamemaker.locales.LanguageBundle", Locale.getDefault());
	/**
	 * Returns the ResourceBundle languageBundle (the locales for the UI)
	 * @return languageBundle
	 */
	public static ResourceBundle getLanguageBundle() {
		return languageBundle;
	}
	
	/**
	 * Sets the language bundle.
	 * Needed in case of a language change
	 * @param b
	 */
	public static void setLanguageBundle(ResourceBundle b) {
		languageBundle = b;
	}
}
