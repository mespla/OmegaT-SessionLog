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

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.omegat.core.Core;
import org.omegat.gui.editor.Document3;
import org.omegat.gui.editor.EditorController;
import org.omegat.gui.editor.EditorTextArea3;

/**
 * Event listener that manages the changes in the edition text area. This class
 * implements a <code>DocumentListener</code> to control the edition text area.
 * All the insertions and deletions are registered to be stored in the
 * productivity tracking log.
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class CaretUpdateListener implements CaretListener{

    /** Productivity plugin object */
    private SessionLogPlugin productivity;
    
    /**
     * Constructor of the class.
     * @param productivity Productivity plugin object
     */
    public CaretUpdateListener(SessionLogPlugin productivity) {
        this.productivity = productivity;
    }

    /**
     * Method launched when the caret is updated.
     * @param e Caret update which triggers this event
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        Document3 doc=((EditorTextArea3)e.getSource()).getOmDocument();
        if(doc!=null){
            //This is called since the method "isEditMode" in EditorController cannot be accessed
            if(((EditorController)Core.getEditor()).getCurrentTranslation()!=null){
                int start_trans=doc.getTranslationStart();
                int end_trans=start_trans+Core.getEditor().getCurrentTranslation().length();
                if(e.getDot()>=start_trans && e.getDot()<=end_trans){
                    productivity.GetLog().CaretUpdate(e.getMark()+1, e.getDot()+1);
                }
            }
        }
    }
}
