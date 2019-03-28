/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.samples.forestCover;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

public class GenerateData {

    private Config config;

    public GenerateData(Config config) {
        this.config = config;
    }

    public void createTrainingAndTestSet() {
        //Creating data set from file
        DataSet dataSet = createDataSet();
        dataSet.shuffle();

        //Splitting main data set to training set (75%) and test set (25%)
        DataSet[] trainingAndTestSet = dataSet.createTrainingAndTestSubsets(75, 25);

        //Saving training set to file
        DataSet trainingSet = trainingAndTestSet[0];
        System.out.println("Saving training set to file...");
        trainingSet.save(config.getTrainingFileName());

        System.out.println("Training set successfully saved!");

        //Normalizing test set
        DataSet testSet = trainingAndTestSet[1];
        System.out.println("Normalizing test set...");

        Normalizer nor = new MaxNormalizer();
        nor.normalize(testSet);

        System.out.println("Saving normalized test set to file...");
        testSet.shuffle();
        testSet.save(config.getTestFileName());
        System.out.println("Normalized test set successfully saved!");
        System.out.println("Training set size: " + trainingSet.getRows().size() + " rows. ");
        System.out.println("Test set size: " + testSet.getRows().size() + " rows. ");
        System.out.println("-----------------------------------------------");

        double percentTraining = (double) trainingSet.getRows().size() * 100.0 / (double) dataSet.getRows().size();
        double percentTest = (double) testSet.getRows().size() * 100.0 / (double) dataSet.getRows().size();
        System.out.println("Training set takes " + formatDecimalNumber(percentTraining) + "% of main data set. ");
        System.out.println("Test set takes " + formatDecimalNumber(percentTest) + "% of main data set. ");

    }

    //Create data set from file
    private DataSet createDataSet() {
        DataSet dataSet = DataSet.createFromFile(config.getDataFilePath(), 54, 7, ",");
        System.out.println("Main data set size: " + dataSet.getRows().size() + " rows. ");

        return dataSet;
    }

    //Formating decimal number to have 3 decimal places
    private String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(3, RoundingMode.HALF_UP).toString();
    }

    //Creating balanced training set with defined maximum sample of each type od tree 
    public void createBalancedTrainingSet(int count) {
        //Creating empety data set
        DataSet balanced = new DataSet(54, 7);
        //Declare counter for all seven type of tree
        int firstType = 0;
        int secondType = 0;
        int thirdType = 0;
        int fourthType = 0;
        int fifthType = 0;
        int sixthType = 0;
        int seventhType = 0;

        DataSet trainingSet = DataSet.load(config.getTrainingFileName());
        List<DataSetRow> rows = trainingSet.getRows();
        System.out.println("Test set size: " + rows.size() + " rows. ");

        for (DataSetRow row : rows) {
            //Taking desired output vector from loaded file
            double[] DesiredOutput = row.getDesiredOutput();
            int index = -1;
            //Find index of number one in output vector. 
            for (int i = 0; i < DesiredOutput.length; i++) {
                if (DesiredOutput[i] == 1.0) {
                    index = i;
                    break;
                }
            }
            //Add row to balanced data set if number of that type of tree is less than maximum
            switch (index + 1) {
                case 1:
                    if (firstType < count) {
                        balanced.addRow(row);
                        firstType++;
                    }
                    break;
                case 2:
                    if (secondType < count) {
                        balanced.addRow(row);
                        secondType++;
                    }
                    break;
                case 3:
                    if (thirdType < count) {
                        balanced.addRow(row);
                        thirdType++;
                    }
                    break;
                case 4:
                    if (fourthType < count) {
                        balanced.addRow(row);
                        fourthType++;
                    }
                    break;
                case 5:
                    if (fifthType < count) {
                        balanced.addRow(row);
                        fifthType++;
                    }
                    break;
                case 6:
                    if (sixthType < count) {
                        balanced.addRow(row);
                        sixthType++;
                    }
                    break;
                case 7:
                    if (seventhType < count) {
                        balanced.addRow(row);
                        seventhType++;
                    }
                    break;
                default:
                    System.out.println("Error with output vector size! ");
            }
        }
        System.out.println("Balanced test set size: " + balanced.getRows().size() + " rows. ");
        System.out.println("Samples per tree: ");
        System.out.println("First type: " + firstType + " samples. ");
        System.out.println("Second type: " + secondType + " samples. ");
        System.out.println("Third type: " + thirdType + " samples. ");
        System.out.println("Fourth type: " + fourthType + " samples. ");
        System.out.println("Fifth type: " + fifthType + " samples. ");
        System.out.println("Sixth type: " + sixthType + " samples. ");
        System.out.println("Seventh type: " + seventhType + " samples. ");

        balanced.save(config.getBalancedFileName());
    }

    public void normalizeBalancedTrainingSet() {
        //Normalizing balanced training set with MaxNormalizer
        DataSet dataSet = DataSet.load(config.getBalancedFileName());
        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(dataSet);

        System.out.println("Saving normalized training data set to file... ");
        dataSet.shuffle();
        dataSet.shuffle();
        dataSet.save(config.getNormalizedBalancedFileName());
        System.out.println("Normalized training data set successfully saved!");
    }
}
