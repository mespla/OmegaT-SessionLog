/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.events;

import org.omegat.core.data.SourceTextEntry;

/**
 *
 * @author miquel
 */
public interface IEntryEventListener {
    /**
     * Called on new file displayed.
     * 
     * @param activeFileName
     *            new active file name
     */
    void onNewFile(String activeFileName);

    /**
     * Called on new entry activated or current entry deactivated.
     * 
     * @param newEntry
     *            new entry instance which activated, or null if entry
     *            deactivated
     */
    void onEntryActivated(SourceTextEntry newEntry);
}
