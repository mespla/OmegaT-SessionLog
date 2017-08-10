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

import static java.nio.file.StandardCopyOption.*;
import org.omegat.plugins.sessionlog.loggers.BaseLogger;
import org.omegat.plugins.sessionlog.loggers.XMLLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;

/**
 * SessionLog plugin main class. This class centralises the session logging.
 * Here, the logger is created containing all the information collected, and the
 * edition timing of each entry is measured.
 * 
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class SessionLogPlugin {

	/** Menu of the plugin. */
	private SessionLogMenu menu;

	/** Logger of the plugin. */
	private static BaseLogger xmllog;

	private String log_path;

	public static BaseLogger getLogger() {
		return xmllog;
	}

	/**
	 * Constructor of the plugin. This constructor registers the
	 * <code>ApplicationEventListenerSessionLog</code> listener (which registers
	 * most of the other listeners) and the handler created to capture all the
	 * messages from the logger.
	 */
	public SessionLogPlugin() {
		log_path = null;
		menu = new SessionLogMenu(this);
		xmllog = new XMLLogger(this);
		CoreEvents.registerApplicationEventListener(
				new ApplicationEventListenerSessionLog(this));
		Logger.getLogger("global").addHandler(new LoggerHandler());
	}

	/**
	 * Method that returns the menu object.
	 * 
	 * @return Returns the menu object of the plugin
	 */
	public SessionLogMenu GetMenu() {
		return menu;
	}

	/**
	 * Returns the current logger .
	 * 
	 * @return XML logger
	 */
	public BaseLogger GetLog() {
		return xmllog;
	}

	/**
	 * Method that initialises the logger. This method initialises the logger by
	 * opening the log file in the root of the project. This method is called
	 * when a new project is selected.
	 */
	public void InitLogging() {
		IntrospectionTools.replaceAutoComplete();
		xmllog.Reset();

		// Flushing the data about the last entry edited
		File dir = new File(
				Core.getProject().getProjectProperties().getProjectRoot()
						+ "/session_logs");
		dir.mkdir();

		Date d = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmssSS");
		StringBuilder sb = new StringBuilder(dir.getAbsolutePath());
		sb.append("/");
		sb.append(dt.format(d));
		sb.append("session.log");
		File f = new File(sb.toString());
		log_path = f.getAbsolutePath();
		try {
			xmllog.NewProject();

			/**
			 * This method is called whenever a project is loaded. Whenever a
			 * new project is loaded, the document (getOmDocument) is changed.
			 * OmegaT signals this using 2 different events: · when a new
			 * project is loaded, InitLogging · when a new file inside a project
			 * is loaded, SegmentChangedListener.onNewFile To minimize code
			 * duplication, onNewFile() will have the responsibility of pointing
			 * the listeners to the correct OmDocument and to log the file
			 * creation.
			 */

			// xmllog.NewFile(Core.getEditor().getCurrentFile());
			SegmentChangedListener.me
					.onNewFile(Core.getEditor().getCurrentFile());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace(System.err);
		}
	}

        /**
	 * Method that stops the logger. This method stops the logger by writing the
	 * current log into a file.
	 */
	public void BackupLogging() {
		if (log_path != null) {
                    try {
                        Path spath = FileSystems.getDefault().getPath(log_path);
                        Path tpath = FileSystems.getDefault().getPath(log_path+".tmp");
                        Files.move(spath, tpath, REPLACE_EXISTING);
                    } catch (FileNotFoundException ex) {}
                    catch (IOException ex) {}
                    try{
                        PrintWriter pw = new PrintWriter(log_path);
                        xmllog.DumpToWriter(pw);
                    } catch (FileNotFoundException ex) {
                        System.err.print("It was not possible to write the log file "+log_path);
                    }
		}
	}
        
	/**
	 * Method that stops the logger. This method stops the logger by writing the
	 * current log into a file.
	 */
	public void StopLogging() {
		if (log_path != null) {
			try {
				PrintWriter pw = new PrintWriter(log_path);
				xmllog.DumpToWriter(pw);
				log_path = null;
			} catch (FileNotFoundException ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	/**
	 * This class implements a handler for the logger in order to capture the
	 * log messages into the session log.
	 */
	private class LoggerHandler extends Handler {
		/**
		 * This method captures the log messages from the logger in order to
		 * include them into the session log. This method takes a logger record
		 * and processes it to be included in the current session log.
		 * 
		 * @param record
		 *            Record from the logger to be processed
		 */
		@Override
		public void publish(LogRecord record) {
			if (xmllog != null) {
				String message;
				String code = null;
				String format;
				ResourceBundle rb = java.util.ResourceBundle
						.getBundle("org/omegat/Bundle", Locale.ENGLISH);
				if (rb.containsKey(record.getMessage())) {
					code = record.getMessage();
					format = rb.getString(record.getMessage());
				} else
					format = record.getMessage();

				if (record.getParameters() == null
						|| record.getParameters().length == 0) {
					message = format;
				} else {
					message = StringUtil.format(format, record.getParameters());
				}
				try {
					xmllog.LoggerEvent(code,
							record.getParameters()[0].toString(), message);
				} catch (Exception ex) {
					xmllog.LoggerEvent(code, null, message);
				}
			}
		}

		/**
		 * This mehtod is not used, since the logger is never flushed.
		 */
		@Override
		public void flush() {
		}

		/**
		 * This mehtod is not used, since the logger is never closed.
		 */
		@Override
		public void close() throws SecurityException {
		}
	}
}
