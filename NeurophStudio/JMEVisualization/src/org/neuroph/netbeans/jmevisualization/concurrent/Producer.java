/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.jmevisualization.concurrent;

import java.util.concurrent.BlockingQueue;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;

/**
 *
 * @author Milos Randjic
 */
public abstract class Producer implements Runnable{
    
    private  BlockingQueue sharedQueue;
    private  NeuralNetAndDataSet neuralNetAndDataSet;

    public Producer(NeuralNetAndDataSet neuralNetAndDataSet) {
        this.neuralNetAndDataSet = neuralNetAndDataSet;
    }    

    public NeuralNetAndDataSet getNeuralNetAndDataSet() {
        return neuralNetAndDataSet;
    }

    public void setNeuralNetAndDataSet(NeuralNetAndDataSet neuralNetAndDataSet) {
        this.neuralNetAndDataSet = neuralNetAndDataSet;
    }

    public BlockingQueue getSharedQueue() {
        return sharedQueue;
    }

    public void setSharedQueue(BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }
    
}
