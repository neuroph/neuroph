/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.data.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.neuroph.data.DataSet;

/**
 *
 * @author zoran
 */
public class SubSampling implements Sampling {
    int percent;

    public SubSampling(int percent) {
        this.percent = percent;
    }
    
    
    
    @Override
    public DataSet[] sample(DataSet dataSet) {
        DataSet[] subSets = new DataSet[2];

        // create array of random idxs
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < dataSet.size(); i++) {
            randoms.add(i);
        }

        Collections.shuffle(randoms);

        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();
                
        // create sample data set
        subSets[0] = new DataSet(inputSize, outputSize);
        int trainingElementsCount = dataSet.size() * percent / 100;
        for (int i = 0; i < trainingElementsCount; i++) {
            int idx = randoms.get(i);
            subSets[0].addRow(dataSet.getRowAt(idx));
        }


        // create rest of rows to data set
        subSets[1] = new DataSet(inputSize, outputSize);
        int testElementsCount = dataSet.size() - trainingElementsCount;
        for (int i = 0; i < testElementsCount; i++) {
            int idx = randoms.get(trainingElementsCount + i);
            subSets[1].addRow(dataSet.getRowAt(idx));
        }

        return subSets;  
    }
    
}
