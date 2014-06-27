/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.datasetgen;

import org.neuroph.core.data.DataSet;

/**
 *
 * @author Milos Randjic
 */
public abstract class DataSetGenerator {
    protected int numberOfPoints;

    public DataSetGenerator() {

    }

    public DataSetGenerator(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }
    
    
    public abstract DataSet generate();

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }
    
}
