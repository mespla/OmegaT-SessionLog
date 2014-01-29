/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.machinetranslators;

import java.util.regex.Pattern;
import org.omegat.util.Language;
import org.omegat.util.Preferences;

/**
 *
 * @author miquel
 */
public class Google2Translate extends BaseTranslate {

    
    protected String getPreferenceName() {
        return Preferences.ALLOW_APERTIUM_TRANSLATE;
    }
    
    public String getName() {
        return "";
    }
    
    public String translate(Language sLang, Language tLang, String text)
            throws Exception {return "";}
}
