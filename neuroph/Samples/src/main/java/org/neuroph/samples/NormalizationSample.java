/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
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

package org.neuroph.samples;

import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.Normalizer;

/**
 * This sample shows how to do data normalization in Neuroph.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NormalizationSample {

    /**
     * Runs this sample
     */    
    public static void main(String[] args) {
        
        // create data set to normalize
        DataSet dataSet = new DataSet(2, 1);
        dataSet.addRow(new DataSetRow(new double[]{10, 12}, new double[]{0}));
        dataSet.addRow(new DataSetRow(new double[]{23, 19}, new double[]{0}));
        dataSet.addRow(new DataSetRow(new double[]{47, 76}, new double[]{0}));
        dataSet.addRow(new DataSetRow(new double[]{98, 123}, new double[]{1}));

        Normalizer norm = new MaxMinNormalizer();
        norm.normalize(dataSet);
        
        // print out normalized training set
        for (DataSetRow dataSetRow : dataSet.getRows()) {
            System.out.print("Input: " + Arrays.toString(dataSetRow.getInput()));
            System.out.print("Output: " + Arrays.toString(dataSetRow.getDesiredOutput()));            
        }
    }
}
