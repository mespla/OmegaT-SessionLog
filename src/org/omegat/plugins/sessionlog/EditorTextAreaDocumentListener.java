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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.omegat.core.Core;

/**
 * Event listener that manages the changes in the edition text area. This class
 * implements a <code>DocumentListener</code> to control the edition text area.
 * 
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class EditorTextAreaDocumentListener implements DocumentListener {

	SessionLogPlugin sessionlog;

	public EditorTextAreaDocumentListener(SessionLogPlugin sessionlog) {
		this.sessionlog = sessionlog;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (Core.getEditor().getCurrentTranslation() != null)
			sessionlog.GetLog().SetLastEditedText(
					Core.getEditor().getCurrentTranslation());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (Core.getEditor().getCurrentTranslation() != null)
			sessionlog.GetLog().SetLastEditedText(
					Core.getEditor().getCurrentTranslation());
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
