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
package org.omegat.core;

import javax.swing.JMenu;

import org.omegat.core.data.IProject;
import org.omegat.gui.matches.IMatcher;
import org.omegat.gui.editor.IEditor;
import org.omegat.gui.editor.mark.IMarker;
import org.omegat.gui.main.IMainMenu;
import org.omegat.gui.main.IMainWindow;
import org.omegat.gui.exttrans.MachineTranslateTextArea;
import org.omegat.gui.glossary.IGlossaries;

public class Core {
    public static IProject getProject() {return null;}
    
    public static IGlossaries getGlossary() {return null;}

    public static IMainWindow getMainWindow() {
        return new IMainWindow() {
            public IMainMenu getMainMenu() {
                return new IMainMenu() {
                    public JMenu getOptionsMenu() {
                        return new JMenu();
                    }
                };
            }
        };
    }

    public static IEditor getEditor() {
        return null;
    }
    
    public static IMatcher getMatcher() {
        return null;
    }
    
    public static void registerMarkerClass(Class<? extends IMarker> clazz) {}
    
    public static MachineTranslateTextArea getMachineTranslatePane() {return null;}
}
