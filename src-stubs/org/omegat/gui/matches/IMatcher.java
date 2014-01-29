/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.gui.matches;

import org.omegat.core.matching.NearString;

/**
 *
 * @author miquel
 */
public interface IMatcher {
    NearString getActiveMatch();

    void setActiveMatch(int activeMatch);
}
