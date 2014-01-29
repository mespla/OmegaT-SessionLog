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

 Copyright (C) 2014 Universitat d'Alacant [www.ua.es]

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.gui.editor.EditorTextArea3;
import org.omegat.gui.glossary.GlossaryTextArea;
import org.omegat.gui.main.MainWindow;
import org.omegat.gui.main.MainWindowMenu;
import org.omegat.gui.matches.MatchesTextArea;

/**
 * Class that extends an <code>IApplicationEventListener</code> object, and
 * performs the actions needed for starting the collection of productivity data.
 * This class overwrites the methods <code>onApplicationStartup</code>, which
 * registers some listeners needed for capturing productivity data, and <code>
 * onApplicationShutdown</code> which creates the XML file containing all the
 * information obtained during the translation.
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class ApplicationEventListenerSessionLog implements IApplicationEventListener{
    
    /** Plugin class from which this class is created */
    SessionLogPlugin productivity;

    /**
     * Overloaded constructor of the class.
     * @param productivity Plugin class from which this class is created
     */
    public ApplicationEventListenerSessionLog(SessionLogPlugin productivity) {
        this.productivity = productivity;
    }

    /**
     * Event launched when the application is started up. This 
     */
    @Override
    public void onApplicationStartup() {
        CoreEvents.registerProjectChangeListener(new ProjectChangesListener(productivity));
        CoreEvents.registerEntryEventListener(new SegmentChangedListener(productivity));
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Field editUndoMenuItemField;
                Field editRedoMenuItemField;
                try{
                    editUndoMenuItemField =
                            MainWindowMenu.class.getDeclaredField("editUndoMenuItem");
                    editRedoMenuItemField =
                            MainWindowMenu.class.getDeclaredField("editRedoMenuItem");
                    
                    //Setting it accessible
                    editUndoMenuItemField.setAccessible(true);
                    editRedoMenuItemField.setAccessible(true);
                    try{
                        JMenuItem editUndoMenuItem;
                        JMenuItem editRedoMenuItem;
                        MainWindowMenu mmenu=(MainWindowMenu)((MainWindow)
                                Core.getMainWindow()).getMainMenu();
                        editUndoMenuItem=(JMenuItem)editUndoMenuItemField.get(mmenu);
                        editRedoMenuItem=(JMenuItem)editRedoMenuItemField.get(mmenu);
                        editUndoMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                productivity.GetLog().Undo();
                            }
                        });
                        editRedoMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                productivity.GetLog().Redo();
                            }
                        });
                    }
                    catch(IllegalAccessException iae){
                        iae.printStackTrace(System.err);
                        System.exit(-1);
                    }
                }
                catch(NoSuchFieldException nsfe){
                    nsfe.printStackTrace(System.err);
                    System.exit(-1);
                }
                
                Core.getGlossary().addMouseListener(new PopupListener(Core.getGlossary()));

                MatchesTextArea matcher=(MatchesTextArea)Core.getMatcher();
                matcher.getDocument().addDocumentListener(new DocumentListener(){
                    //When a change is registered, if the active match changed,
                    //the recommendations are re-computed
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        int activeMatch=IntrospectionTools.getActiveMatchIndex();
                        if(productivity.GetLog().getCurrentTMProposals()!=activeMatch+1){
                            productivity.GetLog().setCurrentTMProposals(activeMatch+1);
                        }
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if(!e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                    productivity.GetLog().setEmtpyTMProposals(false);
                        } catch (BadLocationException ex) {}
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            if(e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                productivity.GetLog().setEmtpyTMProposals(true);
                        } catch (BadLocationException ex) {}
                    }
                });
                
                matcher.getDocument().addDocumentListener(new DocumentListener(){
                    //When a change is registered, if the active match changed,
                    //the recommendations are re-computed
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        int activeMatch=IntrospectionTools.getActiveMatchIndex();
                        if(productivity.GetLog().getCurrentTMProposals()!=activeMatch+1){
                            productivity.GetLog().setCurrentTMProposals(activeMatch+1);
                        }
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if(!e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                    productivity.GetLog().setEmtpyTMProposals(false);
                        } catch (BadLocationException ex) {}
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            if(e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                productivity.GetLog().setEmtpyTMProposals(true);
                        } catch (BadLocationException ex) {}
                    }
                });
                
                Core.getGlossary().getDocument().addDocumentListener(new DocumentListener(){
                    //When a change is registered, if the active match changed,
                    //the recommendations are re-computed
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if(!e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                    productivity.GetLog().setEmtpyGlossaryProposals(false);
                        } catch (BadLocationException ex) {}
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            if(e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                productivity.GetLog().setEmtpyGlossaryProposals(true);
                        } catch (BadLocationException ex) {}
                    }
                });
                
                Core.getMachineTranslatePane().getDocument().addDocumentListener(new DocumentListener(){
                    //When a change is registered, if the active match changed,
                    //the recommendations are re-computed
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if(!e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                    productivity.GetLog().setEmtpyMTProposals(false);
                        } catch (BadLocationException ex) {}
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            if(e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getEndPosition().getOffset()).trim().isEmpty())
                                productivity.GetLog().setEmtpyMTProposals(true);
                        } catch (BadLocationException ex) {}
                    }
                });
            }
        });
    }

    @Override
    public void onApplicationShutdown() {
        productivity.PrintLog(true);
    }
    
    class PopupListener extends MouseAdapter {

        /** Glossary text area on which the mouse event is detected. */
        private GlossaryTextArea glossaryTextArea;

        /**
         * Overloaded constructor of the class.
         * @param gte Glossary text area on which the mouse event is detected
         */
        public PopupListener(GlossaryTextArea gte) {
            super();
            glossaryTextArea = gte;
        }

        /**
         * Class that captures an action when clicking with the mouse.
         * @param e Event launched
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                String selTxt = glossaryTextArea.getSelectedText();
                if (selTxt != null) {
                    EditorTextArea3 text=IntrospectionTools.getEditorTextArea();
                    productivity.GetLog().InsertFromGlossary(text.getCaret().getDot(),
                            selTxt, text.getOmDocument());  
                }
            }
        }
    }
}
