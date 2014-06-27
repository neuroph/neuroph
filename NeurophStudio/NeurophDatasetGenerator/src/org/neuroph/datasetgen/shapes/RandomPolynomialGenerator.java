/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.datasetgen.shapes;

import java.util.ArrayList;
import java.util.Random;
import org.neuroph.core.data.*;
import org.neuroph.datasetgen.DataSetGenerator;

/**
 *
 * @author Milos Randjic
 */
public class RandomPolynomialGenerator extends DataSetGenerator {

    public RandomPolynomialGenerator(int numberOfPoints) {
        super(numberOfPoints);
    }

    private boolean getCategoryMembership(double randomY, double calcY) {
        return calcY < randomY;
    }

    @Override
    public DataSet generate() {
        Random r = new Random();
        DataSet dataSet = new DataSet(2, 1);

        int order = (int) Math.round(r.nextDouble() * 10);
        while (order < 2) {
            order = (int) Math.round(r.nextDouble() * 10);
        }

        ArrayList<Double> coefficients = new ArrayList<Double>(order + 1);
        for (int i = 1; i <= order + 1; i++) {
            coefficients.add(r.nextGaussian());
        }

        for (int i = 1; i <= numberOfPoints; i++) {
            double tempOrder = order;
            double randomX = r.nextGaussian();
            double randomY = r.nextGaussian();
            double calcY = 0;
            double[] desiredOutput = new double[1];

            for (int j = 0; j < coefficients.size(); j++) {
                double degree = 1;
                for (int k = 1; k <= tempOrder; k++) {
                    degree *= randomX;
                }
                calcY += coefficients.get(j) * degree;
                tempOrder--;
            }

            if (getCategoryMembership(randomY, calcY)) {
                desiredOutput[0] = 0;
            } else {
                desiredOutput[0] = 1;
            }

            dataSet.addRow(new DataSetRow(new double[]{randomX, randomY}, desiredOutput));

        }
        dataSet.setColumnNames(new String[]{"X","Y"});
        dataSet.setLabel(toString());
        return dataSet;

    }

    @Override
    public String toString() {
        return "Random Polynomial";
    }
}
