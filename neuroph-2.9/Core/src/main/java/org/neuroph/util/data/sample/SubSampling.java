/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.util.data.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neuroph.core.data.DataSet;

/**
 * This class provides subsampling of a data set, and creates two subsets of a
 * given data set - for training and testing.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SubSampling implements Sampling {

    int percent;

    public SubSampling(int percent) {
        this.percent = percent;
    }

    @Override
    public List<DataSet> sample(DataSet dataSet) {
        List<DataSet> subSets = new ArrayList<>();

        // create array of random idxs
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < dataSet.size(); i++) {
            randoms.add(i);
        }

        Collections.shuffle(randoms);

        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();

        // create sample data set
        DataSet subSet = new DataSet(inputSize, outputSize);
        int trainingElementsCount = dataSet.size() * percent / 100;
        for (int i = 0; i < trainingElementsCount; i++) {
            int idx = randoms.get(i);
            subSet.addRow(dataSet.getRowAt(idx));
        }
        subSets.add(subSet);

        // create rest of rows to data set
        subSet = new DataSet(inputSize, outputSize);
        int testElementsCount = dataSet.size() - trainingElementsCount;
        for (int i = 0; i < testElementsCount; i++) {
            int idx = randoms.get(trainingElementsCount + i);
            subSet.addRow(dataSet.getRowAt(idx));
        }
        subSets.add(subSet);

        return subSets;
    }
    
}
