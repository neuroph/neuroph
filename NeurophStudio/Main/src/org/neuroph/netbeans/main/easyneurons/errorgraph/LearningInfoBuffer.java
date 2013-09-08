/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.main.easyneurons.errorgraph;

import java.util.concurrent.LinkedBlockingQueue;
import org.neuroph.netbeans.main.LearningInfo;

/**
 * http://cs.wellesley.edu/~ecom/lecture/ConsumerProducerRight.html
 * @author zoran
 */
public class LearningInfoBuffer {
//    private volatile LinkedBlockingQueue<LearningInfo> queue=new LinkedBlockingQueue();
//    private int buffSize = 600;
    LearningInfo learningInfo;
   private boolean available = false;
   
   
   
   public synchronized LearningInfo get() {
      while (/*queue.size() == 0*/ !available) {
         try {
            wait();
         }
         catch (InterruptedException e) {
         }
      }
      
      //LearningInfo li = queue.poll();
      
      int iteration = learningInfo.getIteration().intValue();
      double error = learningInfo.getError().doubleValue();
      
      //if (iteration==-1) return null;
      
      LearningInfo liCopy = new LearningInfo(iteration, error);
      //available = false;
      notifyAll();
      return liCopy;
   }
   
   public synchronized void put(LearningInfo value) {
      while (/*queue.size() >= buffSize*/ available) {
         try {
            wait();
         }
         catch (InterruptedException e) { 
         } 
      }
                     
      //queue.add(value);
      learningInfo = value;
      available = true;
      notifyAll();
   }      
    
}
