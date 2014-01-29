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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.omegat.core.Core;
import org.omegat.core.matching.NearString.Scores;
import org.omegat.gui.editor.Document3;

/**
 *
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class EditorTextAreaDocumentFilter extends DocumentFilter{
    
    SessionLogPlugin sessionlog;
    
    public EditorTextAreaDocumentFilter(SessionLogPlugin sessionlog){
        this.sessionlog=sessionlog;
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws
                       BadLocationException {
        super.remove(fb, offset, length);
        Document3 doc=(Document3)fb.getDocument();
        try{
            String text_to_remove=fb.getDocument().getText(offset, length);
            int trans_start=doc.getTranslationStart();
            sessionlog.GetLog().NewDeletion(offset-trans_start, text_to_remove, doc);
        }catch(NullPointerException ex){}
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, string, attr);
        Document3 doc=(Document3)fb.getDocument();
        try{
            int trans_start=doc.getTranslationStart();
            sessionlog.GetLog().NewInsertion(offset-trans_start, string, doc);
        }catch(NullPointerException ex){}
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        Document3 doc=(Document3)fb.getDocument();
        //The text which will be deleted is stored before performing the replacement
        String text_to_remove=doc.getText(offset, length);
        
        int trans_start=doc.getTranslationStart();

        int trans_end=trans_start+Core.getEditor().getCurrentTranslation().length();
        if(Core.getMatcher().getActiveMatch()!=null && 
                Core.getMatcher().getActiveMatch().translation.equals(text)){
            Scores scores=Core.getMatcher().getActiveMatch().scores[0];
            
            if(offset==trans_start && length==trans_end-trans_start){
                sessionlog.GetLog().ReplaceFromTM(offset-trans_start,
                        IntrospectionTools.getActiveMatchIndex(), text_to_remove, text, scores.score,
                        scores.scoreNoStem, scores.adjustedScore, doc);
            }
            else{
                sessionlog.GetLog().InsertFromTM(offset-trans_start,
                        IntrospectionTools.getActiveMatchIndex(), text, scores.score,
                        scores.scoreNoStem, scores.adjustedScore, doc);
            }
        }
        else if(Core.getMachineTranslatePane().getDisplayedTranslation()!=null &&
                Core.getMachineTranslatePane().getDisplayedTranslation().equals(
                text)){
            sessionlog.GetLog().ReplaceFromMT(offset-trans_start,
                    text_to_remove, text, doc);
        }
        else{
            if(length>0)
                sessionlog.GetLog().NewDeletion(offset-trans_start, text_to_remove, doc);
            sessionlog.GetLog().NewInsertion(offset-trans_start, text, doc);
        }
        
        super.replace(fb, offset, length, text, attrs);
    }
}
