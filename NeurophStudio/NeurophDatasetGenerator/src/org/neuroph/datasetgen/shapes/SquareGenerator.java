/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.datasetgen.shapes;

import java.util.Random;
import org.neuroph.core.data.*;
import org.neuroph.datasetgen.DataSetGenerator;

/**
 *
 * @author Milos Randjic
 */
public class SquareGenerator extends DataSetGenerator {

    protected final double squareSideSize;

    public SquareGenerator(int numberOfPoints, double squareSideSize) {
        super(numberOfPoints);
        this.squareSideSize = squareSideSize;
    }

    protected boolean getCategoryMembership(double randomX, double randomY) {
//Math.abs(randomX) >= squareSideSize * 0.25 && Math.abs(randomX) <= squareSideSize * 0.75 && Math.abs(randomY) >= squareSideSize * 0.25 && Math.abs(randomY) <= squareSideSize * 0.75;
        return Math.abs(randomX) >= -squareSideSize / 2 && Math.abs(randomX) <= squareSideSize / 2 && Math.abs(randomY) >= -squareSideSize / 2 && Math.abs(randomY) <= squareSideSize / 2;
    }

    @Override
    public DataSet generate() {
        Random r = new Random();
        DataSet dataSet = new DataSet(2, 1);
        for (int i = 0; i < numberOfPoints; i++) {
            double randomX = r.nextGaussian();
            double randomY = r.nextGaussian();
            double[] desiredOutput = new double[1];
            if (getCategoryMembership(randomX, randomY)) {
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
        return "Square";
    }
}
