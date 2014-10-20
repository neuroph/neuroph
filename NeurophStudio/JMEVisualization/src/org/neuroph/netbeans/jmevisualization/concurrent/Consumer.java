/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.concurrent;

import java.util.concurrent.BlockingQueue;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;

/**
 *
 * @author Milos Randjic
 */
public abstract class Consumer implements Runnable {

    private BlockingQueue sharedQueue;
    private JMEVisualization jmeVisualization;

    public Consumer(JMEVisualization jmeVisualization) {
        this.jmeVisualization = jmeVisualization;
    }

    public JMEVisualization getJmeVisualization() {
        return jmeVisualization;
    }

    public void setJmeVisualization(JMEVisualization jmeVisualization) {
        this.jmeVisualization = jmeVisualization;
    }

    public BlockingQueue getSharedQueue() {
        return sharedQueue;
    }

    public void setSharedQueue(BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }
       
}
