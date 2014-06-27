/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.datasetgen.shapes;

import java.util.Random;
import org.neuroph.core.data.*;


/**
 *
 * @author Milos Randjic
 */
public class MoonGenerator extends RingGenerator {

    public MoonGenerator(int numberOfPoints, double x, double y, double radius) {
        super(numberOfPoints, x, y, radius, radius);
    }

    private double getCategoryMembership(double randomX, double randomY, double offset, double radius) {
        return ((randomX - (x + offset)) * (randomX - (x + offset)) + (randomY - y) * (randomY - y)) / radius;
    }

    @Override
    public DataSet generate() {
        Random r = new Random();
        DataSet dataSet = new DataSet(2, 1);
        for (int i = 0; i < numberOfPoints; i++) {
            double randomX = r.nextGaussian();
            double randomY = r.nextGaussian();
            double[] desiredOutput = new double[1];
            if (getCategoryMembership(randomX, randomY, firstRadius + 0.25 , firstRadius) >= 1 && getCategoryMembership(randomX, randomY, 0.125, secondRadius) <= 1) {
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
        return "Moon";
    }

}
