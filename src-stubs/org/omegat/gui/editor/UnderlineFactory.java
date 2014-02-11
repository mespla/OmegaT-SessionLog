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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter.HighlightPainter;

public class UnderlineFactory {
    public static class WaveUnderline implements HighlightPainter {
        public WaveUnderline(Color c) {
        }

        public void paint(Graphics g, int p0, int p1, Shape bounds,
                JTextComponent c) {
        }
    }
}
