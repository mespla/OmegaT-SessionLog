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

import org.omegat.plugins.sessionlog.loggers.BaseLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.omegat.core.CoreEvents;
import org.omegat.plugins.sessionlog.loggers.XMLLogger;
import org.omegat.util.StaticUtils;

/**
 * Productivity tracking plugin main class. This class centralises the
 * productivity tracking. Here, the XML tree is created containing all the
 * information collected, and the edition timing of each entry is measured.
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class SessionLogPlugin {
   
    /** Menu of the plugin. */
    private SessionLogMenu menu;
    
    /** Logger of the plugin. */
    private BaseLogger xmllog;
    
    /**
     * Constructor of the plugin. This constructor registers the
     * <code>ApplicationEventListenerProductivity</code>
     * listener (which registers most of the other listeners) and the handler
     * created to capture all the messages from the logger.
     */
    public SessionLogPlugin() {
        menu=new SessionLogMenu(this);
        xmllog=new XMLLogger(this);
        CoreEvents.registerApplicationEventListener(
                new ApplicationEventListenerSessionLog(this));
        Logger.getLogger("global").addHandler(new LoggerHandler());
    }
    
    /**
     * Method that returns the menu object.
     * @return Returns the menu object of the plugin
     */
    public SessionLogMenu GetMenu(){
        return menu;
    }
    
    /**
     * Returns the current productivity tracking logger .
     * @return XML productivity tracking logger
     */
    public BaseLogger GetLog(){
        return xmllog;
    }
    
    /**
     * Method that shows a dialog for choosing a path to a file where the log
     * will be writen. This method allows the user to use an output file through
     * a dialog; when the file is choosen, the current log is writen into the
     * file.
     * @param ask If this variable is <code>true</code>, the user is firstly
     * queried about if he/she wants to save the log (if the user responds "no"
     * the method ends without presenting the dialog to the user). Otherwise,
     * the dialog is directly prompted to the used.
     */
    public void PrintLog(boolean ask) {
        GetLog().CloseEntry();
        //If the user decides to store the productivity tracking log...
        boolean save=true;
        if(ask){
            int answer=JOptionPane.showConfirmDialog(null, "Do you want to store the"
                + " productivity log into a file?", "Productivity log",
                JOptionPane.YES_NO_OPTION);
            if(answer != JOptionPane.YES_OPTION)
                save=false;
        }
        if(save){
            //The output file is choosen
            File f;
            do{
                JFileChooser saveFile = new JFileChooser();
                saveFile.showSaveDialog(null);
                f = saveFile.getSelectedFile();
            } while (f!=null && f.exists() && (JOptionPane.showConfirmDialog(null,
                    "The choosen file already exists. Do you want to overwrite it?",
                    "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION));
            if(f!=null){
                try{
                    //Flushing the data about the last entry edited
                    PrintWriter pw=new PrintWriter(f.getAbsolutePath());
                    GetLog().DumpToWriter(pw);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }
    
    /**
     * This class implements a handler for the logger in order to capture the
     * log messages into the productivity tracking log.
     */
    private class LoggerHandler extends Handler{
        /**
         * This method captures the log messages from the logger in order to
         * include them into the productivity tracking log. This method takes
         * a logger record and processes it to be included in the current
         * productivity tracking log.
         * @param record Record from the logger to be processed
         */
        @Override
        public void publish(LogRecord record) {
            if(xmllog!=null){
                String message;
                String code=null;
                String format;
                ResourceBundle rb=java.util.ResourceBundle.getBundle(
                        "org/omegat/Bundle", Locale.ENGLISH);
                if(rb.containsKey(record.getMessage())){
                    code=record.getMessage();
                    format = rb.getString(record.getMessage());
                }
                else
                    format = record.getMessage();

                if (record.getParameters() == null || record.getParameters().length==0) {
                    message = format;
                } else {
                    message = StaticUtils.format(format, record.getParameters());
                }
                try{
                    xmllog.LoggerEvent(code, record.getParameters()[0].toString(), message);
                }catch (Exception ex){
                    xmllog.LoggerEvent(code, null, message);
                }
            }
        }

        /**
         * This mehtod is not used, since the logger is never flushed.
         */
        @Override
        public void flush() {}

        /**
         * This mehtod is not used, since the logger is never closed.
         */
        @Override
        public void close() throws SecurityException {}
    }
}
