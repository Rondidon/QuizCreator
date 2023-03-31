package com.quizcreator.app.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Locales {
	/**
	 * Returns the ResourceBundle languageBundle (the locales for the UI)
	 * @return languageBundle
	 */
	public static ResourceBundle getGUIBundle() {
		return ResourceBundle.getBundle("com.quizcreator.app.i18n.GUI", Locale.getDefault());
	}
}
