/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.bci.mindwave.event;

import org.neuroph.bci.mindwave.MindWaveSample;

/**
 *
 * @author zoran
 */
public class MindWaveEvent extends java.util.EventObject{
    MindWaveSample packet;
    
    /**
     * TODO: put mind wave packet here
     * @param source
     */
    public MindWaveEvent(Object source, MindWaveSample packet) {
       super(source);
       this.packet = packet;
    }

    public MindWaveSample getPacket() {
        return packet;
    }
    
    
    
}
