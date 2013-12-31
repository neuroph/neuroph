package org.neuroph.bci.mindwave;

import org.neuroph.bci.mindwave.MindWaveTopComponent.MindWaveThread;

/**
 * Provides communication between different mind wave components (windows)
 * Stores settings (training set scaleFactors) , provides access to mind wave thread etc.
 * @author zoran
 */
public class MindWaveManager {
    double[] scaleFactors;
    MindWaveThread mindWaveThread;
    
    
 private static MindWaveManager instance = new MindWaveManager();    
    
    private MindWaveManager() {
    }
    
    public static MindWaveManager getInstance() {
        return instance;
    }

    public double[] getScaleFactors() {
        return scaleFactors;
    }

    public void setScaleFactors(double[] scaleFactors) {
        this.scaleFactors = scaleFactors;
    }

    public MindWaveThread getMindWaveThread() {
        return mindWaveThread;
    }

    public void setMindWaveThread(MindWaveThread mindWaveThread) {
        this.mindWaveThread = mindWaveThread;
    }
    
    
    
    
    
      
    
}
