/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.machinetranslators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.util.Language;

/**
 *
 * @author miquel
 */
public class BaseTranslate implements IMachineTranslation, ActionListener{
    public String getTranslation(Language sLang, Language tLang, String text)
            throws Exception {return "";}

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
