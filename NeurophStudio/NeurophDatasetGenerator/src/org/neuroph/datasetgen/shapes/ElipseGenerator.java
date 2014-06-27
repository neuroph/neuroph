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
public class ElipseGenerator extends DataSetGenerator {

    private final double x;
    private final double y;
    private final double a;
    private final double b;

    public ElipseGenerator(int numberOfPoints, double x, double y, double a, double b) {
        super(numberOfPoints);
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
    }

    private double getCategoryMembership(double randomX, double randomY, double x, double y, double a, double b) {
        return (randomX - x) * (randomX - x) / (a * a) + (randomY - y) * (randomY - y) / (b * b);
    }

    @Override
    public DataSet generate() {
        Random r = new Random();
        DataSet dataSet = new DataSet(2, 1);
        for (int i = 0; i < numberOfPoints; i++) {
            double randomX = r.nextGaussian();
            double randomY = r.nextGaussian();
            double[] desiredOutput = new double[1];
            if (getCategoryMembership(randomX, randomY, x, y, a, b)<=1) {
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
        return "Elipse";
    }
    
    

}
