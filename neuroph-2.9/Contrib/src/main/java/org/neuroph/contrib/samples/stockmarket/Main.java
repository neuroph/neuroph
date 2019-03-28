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

package org.neuroph.contrib.samples.stockmarket;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;

/**
 * Main class which runs the stock market prediction sample - creates and trains neural network for stock prediction.
 * See http://neuroph.sourceforge.net/tutorials/StockMarketPredictionTutorial.html
 * @author Dr.V.Steinhauer
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Time stamp N1:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));

        int maxIterations = 10000;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(4, 9, 1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.001);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.7);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        DataSet trainingSet = new DataSet(4, 1);

        double daxmax = 10000.0D;
        trainingSet.addRow(new DataSetRow(new double[]{3710.0D / daxmax, 3690.0D / daxmax, 3890.0D / daxmax, 3695.0D / daxmax}, new double[]{3666.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3690.0D / daxmax, 3890.0D / daxmax, 3695.0D / daxmax, 3666.0D / daxmax}, new double[]{3692.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3890.0D / daxmax, 3695.0D / daxmax, 3666.0D / daxmax, 3692.0D / daxmax}, new double[]{3886.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3695.0D / daxmax, 3666.0D / daxmax, 3692.0D / daxmax, 3886.0D / daxmax}, new double[]{3914.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3666.0D / daxmax, 3692.0D / daxmax, 3886.0D / daxmax, 3914.0D / daxmax}, new double[]{3956.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3692.0D / daxmax, 3886.0D / daxmax, 3914.0D / daxmax, 3956.0D / daxmax}, new double[]{3953.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3886.0D / daxmax, 3914.0D / daxmax, 3956.0D / daxmax, 3953.0D / daxmax}, new double[]{4044.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3914.0D / daxmax, 3956.0D / daxmax, 3953.0D / daxmax, 4044.0D / daxmax}, new double[]{3987.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3956.0D / daxmax, 3953.0D / daxmax, 4044.0D / daxmax, 3987.0D / daxmax}, new double[]{3996.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3953.0D / daxmax, 4044.0D / daxmax, 3987.0D / daxmax, 3996.0D / daxmax}, new double[]{4043.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{4044.0D / daxmax, 3987.0D / daxmax, 3996.0D / daxmax, 4043.0D / daxmax}, new double[]{4068.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3987.0D / daxmax, 3996.0D / daxmax, 4043.0D / daxmax, 4068.0D / daxmax}, new double[]{4176.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{3996.0D / daxmax, 4043.0D / daxmax, 4068.0D / daxmax, 4176.0D / daxmax}, new double[]{4187.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{4043.0D / daxmax, 4068.0D / daxmax, 4176.0D / daxmax, 4187.0D / daxmax}, new double[]{4223.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{4068.0D / daxmax, 4176.0D / daxmax, 4187.0D / daxmax, 4223.0D / daxmax}, new double[]{4259.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{4176.0D / daxmax, 4187.0D / daxmax, 4223.0D / daxmax, 4259.0D / daxmax}, new double[]{4203.0D / daxmax}));
        trainingSet.addRow(new DataSetRow(new double[]{4187.0D / daxmax, 4223.0D / daxmax, 4259.0D / daxmax, 4203.0D / daxmax}, new double[]{3989.0D / daxmax}));
        neuralNet.learn(trainingSet);
        System.out.println("Time stamp N2:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));

        DataSet testSet = new DataSet(4, 1); 
        testSet.addRow(new DataSetRow(new double[]{4223.0D / daxmax, 4259.0D / daxmax, 4203.0D / daxmax, 3989.0D / daxmax}));

        for (DataSetRow testDataRow : testSet.getRows()) {
            neuralNet.setInput(testDataRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            System.out.print("Input: " + Arrays.toString(testDataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) );
        }

        //Experiments:
        //                   calculated
        //31;3;2009;4084,76 -> 4121 Error=0.01 Rate=0.7 Iterat=100
        //31;3;2009;4084,76 -> 4096 Error=0.01 Rate=0.7 Iterat=1000
        //31;3;2009;4084,76 -> 4093 Error=0.01 Rate=0.7 Iterat=10000
        //31;3;2009;4084,76 -> 4108 Error=0.01 Rate=0.7 Iterat=100000
        //31;3;2009;4084,76 -> 4084 Error=0.001 Rate=0.7 Iterat=10000

        System.out.println("Time stamp N3:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));
        System.exit(0);
    }
}
