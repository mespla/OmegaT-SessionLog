/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.omegat.gui.editor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

/**
 *
 * @author mespla
 */
public class DocumentFilter3 extends DocumentFilter{
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {}
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{}
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{}
}
