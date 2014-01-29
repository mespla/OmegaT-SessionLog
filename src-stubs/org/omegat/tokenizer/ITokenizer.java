/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.tokenizer;

import org.omegat.util.Token;

/**
 *
 * @author miquel
 */
public interface ITokenizer {
    Token[] tokenizeAllExactly(String str);
}
