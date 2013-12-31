package org.neuroph.bci.mindwave.event;

/**
 * Listener for MindWave events
 * @author Zoran Sevarac
 */
public interface MindWaveEventListener extends  java.util.EventListener {   
    
    public void handleMindWaveEvent(MindWaveEvent event);    
    
}
