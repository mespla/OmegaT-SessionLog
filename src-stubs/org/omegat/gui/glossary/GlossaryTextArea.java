/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.glossary;

import java.util.List;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.common.EntryInfoThreadPane;

/**
 *
 * @author miquel
 */
public class GlossaryTextArea extends EntryInfoThreadPane<List<GlossaryEntry>>{

    @Override
    public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {}

    @Override
    public void onNewFile(String activeFileName) {}

    @Override
    public void onEntryActivated(SourceTextEntry newEntry) {}
}
