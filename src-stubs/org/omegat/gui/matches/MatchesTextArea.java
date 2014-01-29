/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.matches;

import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.matching.NearString;
import org.omegat.gui.common.EntryInfoThreadPane;

/**
 *
 * @author miquel
 */
public class MatchesTextArea extends EntryInfoThreadPane<List<NearString>> implements IMatcher {
    public Document getDocument() {return null;}
    
    public NearString getActiveMatch() {return null;}

    public void select(int a, int b){}
    
    public void setCharacterAttributes(AttributeSet attr, boolean replace) {}

    @Override
    public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
    }

    @Override
    public void onNewFile(String activeFileName) {
    }

    @Override
    public void onEntryActivated(SourceTextEntry newEntry) {
    }

    @Override
    public void setActiveMatch(int activeMatch) {
    }
}
