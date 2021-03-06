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

import org.omegat.core.Core;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.events.IEntryEventListener;

/**
 * Class that manages the activation of an entry. This class extends the <code>
 * IEntryEventListener</code> controls the activateion of an entry in the text
 * area where entries are shown. When a new entry is activated, the variables
 * controling the actions on the editino text area and the matchig text area are
 * restarted and the <code>TextAreaDocumentListener</code> is reset.
 * 
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class SegmentChangedListener implements IEntryEventListener {

	/** Caret update listener. */
	private CaretUpdateListener caret_listener;

	/**
	 * Document filter that controls the editions on the segments before they
	 * are performed.
	 */
	private EditorTextAreaDocumentFilter filter;

	/** SessionLog plugin class. */
	private SessionLogPlugin sessionlog;

	/**
	 * Document listener that controls the editions on the segments after they
	 * are performed.
	 */
	private EditorTextAreaDocumentListener text_area_listener;

	/** The (potentially) only instance of this class */
	public static SegmentChangedListener me;

	/** Keep track if we injected the listeners */
	private boolean isInjected = false;

	/**
	 * Overloaded constructor.
	 * 
	 * @param sessionlog
	 *            SessionLog plugin class
	 */
	public SegmentChangedListener(SessionLogPlugin sessionlog) {
		this.filter = null;
		this.sessionlog = sessionlog;
		this.caret_listener = null;
		this.text_area_listener = null;

		if (me != null) {
			System.err.println("SegmentChangedListener is not unique.");
			System.exit(-1);
		}
		me = this;

		if (caret_listener == null) {
			caret_listener = new CaretUpdateListener(sessionlog);
			IntrospectionTools.getEditorTextArea()
					.addCaretListener(caret_listener);
		}
	}

	/**
	 * Event launched when a new file is chosen.
	 * 
	 * @param activeFileName
	 *            Name of the file opened
	 */
	@Override
	public void onNewFile(String activeFileName) {

		/**
		 * OmegaT does not notify any change of segment when loading a new file
		 * or project. Logger.closeProject() handles this, but there is no
		 * Logger.closeFile().
		 */
		sessionlog.GetLog().CloseEntry();

		System.err.println("\t\tNew file " + activeFileName);
		sessionlog.GetLog().NewFile(activeFileName);

		isInjected = false;
	}

	public void injectListeners() {
		isInjected = true;
		if (filter == null) {
			filter = new EditorTextAreaDocumentFilter(sessionlog);
		}
		IntrospectionTools.getEditorTextArea().getOmDocument()
				.setDocumentFilter(filter);

		if (text_area_listener == null) {
			text_area_listener = new EditorTextAreaDocumentListener(sessionlog);
		}
		IntrospectionTools.getEditorTextArea().getOmDocument()
				.addDocumentListener(text_area_listener);
	}

	/**
	 * Method launched when an entry is activated.
	 * 
	 * @param newEntry
	 *            Entry which has been activated.
	 */
	@Override
	public void onEntryActivated(SourceTextEntry newEntry) {
		if (sessionlog.GetLog().GetCurrentSegmentNumber() != Core.getEditor()
				.getCurrentEntry().entryNum()) {

			/**
			 * Inject the listeners as late as possible. When the first project
			 * is loaded after OmegaT startup, OmDocument does not exist when
			 * InitLogging is called (subsequent project loads do not trigger
			 * this behaviour).
			 */
			if (!isInjected) {
                            injectListeners();
			}

			sessionlog.GetLog().CloseEntry();
                        sessionlog.BackupLogging();
			sessionlog.GetLog().NewEntry(newEntry);
		}
	}
}
