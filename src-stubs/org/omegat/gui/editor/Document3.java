/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.editor;

import javax.swing.text.DefaultStyledDocument;

/**
 *
 * @author miquel
 */
@SuppressWarnings("serial")
public class Document3 extends DefaultStyledDocument {
    boolean isEditMode(){ return false; }
    
    public int getTranslationStart(){return 0;}
}
