/**************************************************************************
 Plugins for OmegaT(http://www.omegat.org/)
 
 This code is only a stub, and is based on the code created for the languagetool
 plugin for OmegaT: Copyright (C) 2008 Alex Buloichik (alex73mail@gmail.com)

 This cose is released under the GPLv3:
 *************************************************************************
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *************************************************************************/
package org.omegat.gui.editor;

import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.main.MainWindow;

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
