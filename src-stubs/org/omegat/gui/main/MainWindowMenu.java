/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author miquel
 */
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
