/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.events;

/**
 *
 * @author miquel
 */
public interface IApplicationEventListener {
    public void onApplicationStartup();
    public void onApplicationShutdown();
}
