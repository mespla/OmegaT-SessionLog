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
package org.omegat.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainWindowMenu implements ActionListener, MenuListener, IMainMenu {
    
    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void menuSelected(MenuEvent e) {}

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

    @Override
    public JMenu getOptionsMenu() {
        return null;
    }
}
