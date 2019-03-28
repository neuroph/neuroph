/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.art;

import org.neuroph.core.Neuron;

/**
 *
 * @author ja
 */
public class ART1CompetitiveNeuron extends Neuron {

    /**
     * In the third ART layer, neurons can have three states:
     * active, inactive and inhibited
     */
    private boolean active = false;
    private boolean inhibited = false;

    /**
     * Returns true if neuron is active, false otherwise.
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public boolean isInhibited() {
        return inhibited;
    }

    /**
     *
     * @param inhibited
     */
    public void setInhibited(boolean inhibited) {
        this.inhibited = inhibited;
    }

}
