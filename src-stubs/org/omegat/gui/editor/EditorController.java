/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.editor;

import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.main.MainWindow;

/**
 *
 * @author miquel
 */
public class EditorController implements IEditor{

    protected SegmentBuilder[] m_docSegList;
    protected int displayedEntryIndex;

    public EditorController(final MainWindow mainWindow) {}

    @Override
    public SourceTextEntry getCurrentEntry() {return null;}

    @Override
    public void remarkOneMarker(final String markerClassName) {}

    @Override
    public String getCurrentTranslation() {return null;}
    
    @Override
    public String getCurrentFile() {return null;}

    @Override
    public String getSelectedText() {return null;}
}
