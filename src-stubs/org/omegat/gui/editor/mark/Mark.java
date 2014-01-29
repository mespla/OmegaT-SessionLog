/**************************************************************************
 Plugins for OmegaT(http://www.omegat.org/)
 
 This code is only a stub.
 
 Copyright (C) 2008 Alex Buloichik (alex73mail@gmail.com)

 Code is released under the dual licenses of the GPLv3 and Apache License v2.0
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
 *************************************************************************
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 *************************************************************************/
package org.omegat.gui.editor.mark;

import javax.swing.text.Highlighter.HighlightPainter;

public class Mark {
    public String toolTipText;
    
    public final int startOffset, endOffset;
    
    public HighlightPainter painter;
    
    public enum ENTRY_PART {
        SOURCE, TRANSLATION
    };

    public Mark(ENTRY_PART entryPart, int start, int end) {
        this.startOffset = start;
        this.endOffset = end;
    }
}
