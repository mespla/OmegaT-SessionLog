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

import org.omegat.core.events.IProjectEventListener;

/**
 * Project event listener that captures the event launched when any change is
 * performed on an OmegaT project.
 * 
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class ProjectChangesListener implements IProjectEventListener {

	/** SessionLog plugin object. */
	private SessionLogPlugin sessionlog;

	/**
	 * Constructor of the class.
	 * 
	 * @param sessionlog
	 *            SessionLog plugin object
	 */
	public ProjectChangesListener(SessionLogPlugin sessionlog) {
		this.sessionlog = sessionlog;
	}

	/**
	 * Method that captures the event launched when a change is performe done an
	 * OmegaT project.
	 * 
	 * @param eventType
	 *            Type of the changed perfomred on the project
	 */
	@Override
	public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
		sessionlog.GetMenu().SetEnabledLogging(true);
		if (eventType == PROJECT_CHANGE_TYPE.CLOSE) {
			sessionlog.GetLog().CloseProject();
			if (sessionlog.GetMenu().isLoggerSelected()) {
				sessionlog.StopLogging();
				sessionlog.GetMenu().SetEnabledPauseTiming(false);
			}
		} else if (eventType == PROJECT_CHANGE_TYPE.LOAD
				|| eventType == PROJECT_CHANGE_TYPE.CREATE) {
			if (sessionlog.GetMenu().isLoggerSelected()) {
				sessionlog.InitLogging();
				sessionlog.GetMenu().SetEnabledPauseTiming(true);
			}
		}
	}
}
