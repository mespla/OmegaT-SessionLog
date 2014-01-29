/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.machinetranslators;

import java.util.regex.Pattern;
import org.omegat.util.Preferences;

/**
 *
 * @author miquel
 */
public class ApertiumTranslate extends BaseTranslate {
    public static String GT_URL2;
    public static String GT_URL;
    protected static Pattern RE_HTML;
    protected static Pattern RE_DETAILS;
    protected static Pattern RE_STATUS;
    protected static Pattern RE_UNICODE;
    public static String MARK_BEG;
    public static String MARK_END;
    
    protected String getPreferenceName() {
        return Preferences.ALLOW_APERTIUM_TRANSLATE;
    }
    
    public String getName() {
        return "";
    }

}
