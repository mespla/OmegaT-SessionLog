/******************************************************************************
 SessionLog OmegaT plugin - Plugin for OmegaT (htpp://www.omegat.org) to track
                            the actions of a user during translation and storing
                            the log in an XML file.
                            This plugin keeps track of all the editions
                            performed by a user during the translation of a
                            project. The plugin works in a transparent way for
                            the user and all the information obtained is stored
                            in an XML file which can be created when the tool
                            is closed.

 Copyright (C) 2013-2014 Universitat d'Alacant [www.ua.es]

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 **************************************************************************/
package org.omegat.plugins.sessionlog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.core.machinetranslators.MachineTranslators;
import org.omegat.core.matching.NearString;
import org.omegat.gui.editor.Document3;
import org.omegat.gui.editor.EditorController;
import org.omegat.gui.editor.EditorTextArea3;
import org.omegat.gui.editor.TranslationUndoManager;
import org.omegat.gui.editor.autocompleter.AutoCompleter;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.gui.exttrans.MachineTranslateTextArea;
import org.omegat.gui.glossary.GlossaryEntry;
import org.omegat.gui.glossary.GlossaryTextArea;
import org.omegat.gui.matches.MatchesTextArea;
import org.omegat.plugins.sessionlog.instrumented.InstrumentedAutoComplete;

/**
 * Class that contains a set of methods that use introspection for getting the
 * value of some of the private variables in OmegaT.
 * 
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class IntrospectionTools {

	/** Editor text area in the main window. */
	private static EditorTextArea3 editor_text_area = null;

	/**
	 * Method that returns the EditorTextArea3 object from <code>Core</code>.
	 * This method uses introspection to acces the private EditorTextArea3
	 * object in <code>Core</code> and return it. This should be idealy accessed
	 * in a different way (without introspection) but it is the only possibility
	 * by now.
	 * 
	 * @return Returns the EditorTextArea3 object from <code>Core</code>
	 */
	public static EditorTextArea3 getEditorTextArea() {
		if (editor_text_area == null) {
			EditorController controller = (EditorController) Core.getEditor();

			// Getting the field
			Field editor;
			try {
				editor = EditorController.class.getDeclaredField("editor");
				// Setting it accessible
				editor.setAccessible(true);
				try {
					editor_text_area = (EditorTextArea3) editor.get(controller);
				} catch (IllegalAccessException iae) {
					iae.printStackTrace(System.err);
					System.exit(-1);
				}
			} catch (NoSuchFieldException nsfe) {
				nsfe.printStackTrace(System.err);
				System.exit(-1);
			}
		}

		return editor_text_area;
	}

	/**
	 * Method that returns the EditorTextArea3 object from <code>Core</code>.
	 * This method uses introspection to acces the private EditorTextArea3
	 * object in <code>Core</code> and return it. This should be idealy accessed
	 * in a different way (without introspection) but it is the only possibility
	 * by now.
	 * 
	 * @return Returns the EditorTextArea3 object from <code>Core</code>
	 */
	public static String getCurrentTranslation() {
		String ret = null;

		Document3 doc = IntrospectionTools.getEditorTextArea().getOmDocument();
		Method m;

		try {
			m = Document3.class.getDeclaredMethod("extractTranslation");
			m.setAccessible(true);
			ret = (String) m.invoke(doc);
		} catch (NoSuchMethodException | IllegalAccessException
				| InvocationTargetException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return ret;
	}

	/**
	 * Method that returns the index of the active match from
	 * <code>MatchesTextArea</code>. This method uses introspection to acces the
	 * private MatchesTextArea object in <code>Core</code> and returns the index
	 * of the active match. This should be idealy accessed in a different way
	 * (without introspection) but this is the only possibility by now.
	 * 
	 * @return Returns the index of the active match from
	 *         <code>MatchesTextArea</code>.
	 */
	public static int getActiveMatchIndex() {
		int activeMatch = -1;
		try {
			Field actMatch = MatchesTextArea.class
					.getDeclaredField("activeMatch");
			actMatch.setAccessible(true);
			try {
				activeMatch = (Integer) actMatch.get(Core.getMatcher());
			} catch (IllegalAccessException iae) {
				iae.printStackTrace(System.err);
				System.exit(-1);
			}
		} catch (NoSuchFieldException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return activeMatch;
	}

	public static boolean undoInProgress() {
		Field undoManagerField;
		try {
			undoManagerField = EditorTextArea3.class
					.getDeclaredField("undoManager");
			undoManagerField.setAccessible(true);
			try {
				TranslationUndoManager undoManager = (TranslationUndoManager) undoManagerField
						.get(getEditorTextArea());
				if (undoManager != null) {
					Field inProgressField;
					inProgressField = TranslationUndoManager.class
							.getDeclaredField("inProgress");
					inProgressField.setAccessible(true);
					return (Boolean) inProgressField.get(undoManager);
				}
			} catch (IllegalAccessException iae) {
				iae.printStackTrace(System.err);
				System.exit(-1);
			}
		} catch (NoSuchFieldException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return false;
	}

	public static List<NearString> getMatches() {
		List<NearString> matches = null;
		try {
			Field matchesField = MatchesTextArea.class
					.getDeclaredField("matches");
			matchesField.setAccessible(true);
			try {
				matches = (List<NearString>) matchesField
						.get((MatchesTextArea) Core.getMatcher());
			} catch (IllegalAccessException iae) {
				iae.printStackTrace(System.err);
				System.exit(-1);
			}
		} catch (NoSuchFieldException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return matches;
	}

	public static List<GlossaryEntry> getGlossaryEntries() {
		List<GlossaryEntry> nowEntries = null;
		try {
			Field nowEntriesField = GlossaryTextArea.class
					.getDeclaredField("nowEntries");
			nowEntriesField.setAccessible(true);
			try {
				nowEntries = (List<GlossaryEntry>) nowEntriesField
						.get(Core.getGlossary());
			} catch (IllegalAccessException iae) {
				iae.printStackTrace(System.err);
				System.exit(-1);
			}
		} catch (NoSuchFieldException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return nowEntries;
	}

	public static int getMTEntriesSize() {
		try {
			
			List<IMachineTranslation> translators = MachineTranslators.getMachineTranslators();
			int counter = 0;
			Field enabledField = BaseTranslate.class
					.getDeclaredField("enabled");
			enabledField.setAccessible(true);
			for (IMachineTranslation mt : translators) {
				boolean enabled = (Boolean) enabledField
						.get((BaseTranslate) mt);
				if (enabled)
					counter++;
			}
			return counter;
		} catch (NoSuchFieldException | IllegalAccessException nsfe) {
			nsfe.printStackTrace(System.err);
			System.exit(-1);
		}

		return -1;
	}

	public static void replaceAutoComplete() {
		EditorTextArea3 editor = getEditorTextArea();
		Field f;
		AutoCompleter ac;
		InstrumentedAutoComplete iac;

		try {
			f = EditorTextArea3.class.getDeclaredField("autoCompleter");
			f.setAccessible(true);
			ac = (AutoCompleter) f.get(editor);
			iac = new InstrumentedAutoComplete(ac, editor);
			f.set(editor, iac);
		} catch (NoSuchFieldException | IllegalAccessException ex) {
			System.out.println("SessionLog error: replacing the autocompleter");
		}

	}
}
