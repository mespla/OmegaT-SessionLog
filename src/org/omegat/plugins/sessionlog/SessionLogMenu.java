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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.gui.matches.MatchesTextArea;

/**
 * Class that manages the time pausing option. This class creates a "time
 * pausing" option in the "options" menu of the main menu of OmegaT. When this
 * option is choosed, the entry edition timing is stopped. During a pause, the
 * edition text are is disabled, so no changes can be done on the translation.
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class SessionLogMenu {
    
    private SessionLogPlugin sessionlog;

    /** Option for pausing the timing. */
    private final JCheckBoxMenuItem pausetiming;
    
    private final JCheckBoxMenuItem enable_logging;
    
    public void SetEnabledLogging(boolean enabled){
        enable_logging.setEnabled(enabled);
    }
    
    public void SetEnabledPauseTiming(boolean enabled){
        pausetiming.setEnabled(enabled);
    }
    
    /** Timestamp at the moment of the pause */
    private long pause_timestamp;

    /**
     * Method that sets a timestamp marking the beggining of a pause.
     * @param timestamp Timestamp marking the beggining of a pause
     */
    public void setPauseTimestamp(long timestamp) {
        this.pause_timestamp = timestamp;
    }

    /**
     * Method that returns the object in the menu used by the user to set a pause.
     * @return Returns the <code>JCheckBoxMenuItem</code> object that is used by the user to set a pause
     */
    public JCheckBoxMenuItem getPausetiming() {
        return pausetiming;
    }
    
    public boolean isLoggerSelected(){
        return enable_logging.isSelected();
    }
    
    /**
     * Constructor of the class, which adds the option to the menu.
     * @param sessionlog Object that controls the coloring in the matcher.
     */
    public SessionLogMenu(SessionLogPlugin sessionlog) {
        this.sessionlog=sessionlog;
        this.pause_timestamp=0;
        
        this.pausetiming = new JCheckBoxMenuItem("Pause timing in SessionLog");
        this.pausetiming.addActionListener(pausetimingMenuItemActionListener);
        this.pausetiming.setSelected(false);
        this.pausetiming.setEnabled(false);
        
        
        this.enable_logging = new JCheckBoxMenuItem("Enable SessionLog");
        this.enable_logging.addActionListener(enableloggerMenuItemActionListener);
        this.enable_logging.setName("dump_log");
        this.enable_logging.setSelected(true);
        this.enable_logging.setEnabled(false);
        
        CoreEvents.registerApplicationEventListener(new IApplicationEventListener(){
            @Override
            public void onApplicationStartup() {
                Core.getMainWindow().getMainMenu().getOptionsMenu().add(pausetiming);
                Core.getMainWindow().getMainMenu().getOptionsMenu().add(enable_logging);
            }

            @Override
            public void onApplicationShutdown() {
            }
        });
    }

    /**
     * Method applied when a pause is started. This method records the current
     * timestamp and makes the different panels and text areas invisible.
     */
    public void pauseTimingSelected(){
        pause_timestamp = System.nanoTime();
        IntrospectionTools.getEditorTextArea().setVisible(false);
        ((MatchesTextArea)Core.getMatcher()).setVisible(false);
        Core.getGlossary().setVisible(false);
        Core.getMachineTranslatePane().setVisible(false);
    }

    /**
     * Method applied when the work is resumed after a pause. This method is
     * used when a pause is resumed, and registers the pause event in the log,
     * counting the time consumed in the pause, and makes the different panels
     * and text areas visible again.
     */
    public void resumeTimingSelected(){
        sessionlog.GetLog().SetPause(System.nanoTime()-pause_timestamp);
        IntrospectionTools.getEditorTextArea().setVisible(true);
        ((MatchesTextArea)Core.getMatcher()).setVisible(true);
        Core.getGlossary().setVisible(true);
        Core.getMachineTranslatePane().setVisible(true);
    }
    
    /**
     * Action listener that captures the action performed when the menu option
     * for pausing/resuming is activated.
     */
    protected ActionListener pausetimingMenuItemActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(pausetiming.isSelected())
                pauseTimingSelected();
            else
                resumeTimingSelected();
        }
    };
    
    /**
     * Action listener that captures the action performed when the menu option
     * for dumping the session log to a file is activated.
     */
    protected ActionListener enableloggerMenuItemActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(enable_logging.isSelected()){
                pausetiming.setEnabled(true);
                sessionlog.InitLogging();
            }
            else{
                pausetiming.setEnabled(false);
                sessionlog.StopLogging();
            }
        }
    };
}
