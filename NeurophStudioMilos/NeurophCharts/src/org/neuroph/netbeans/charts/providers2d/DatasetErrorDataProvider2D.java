/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.charts.providers2d;

import org.nugs.graph2d.api.DataProvider2D;
import java.awt.geom.Point2D;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetErrorDataProvider2D implements DataProvider2D {

    DataSet dataSet;
    NeuralNetwork nnet;
    Point2D[] error;
    int attribute;

    public DatasetErrorDataProvider2D() {
    }

    public DatasetErrorDataProvider2D(DataSet dataSet, NeuralNetwork nnet) {
        this.dataSet = dataSet;
        this.nnet = nnet;
    }

    @Override
    public Point2D[] getData() {
         int numberOfPoints = dataSet.getRows().size();
        error = new Point2D[numberOfPoints + 1];
        int counter = 1;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            DataSetRow row = rows.get(i);
            nnet.setInput(row.getInput());
            nnet.calculate();
            Neuron outputNeuron = nnet.getOutputNeurons()[0];
                double err = row.getDesiredOutput()[0] - outputNeuron.getOutput();
                double colVal = rows.get(i).getInput()[attribute - 1];
                error[counter] = new Point2D.Double(colVal, err);
                counter++;            
        }

        return error;
    }

    @Override
    public String getXLabel() {
        return "Chosen input";
    }
     @Override
    public String getYLabel() {
        return "Error";
    }

    @Override
    public String toString() {
        return "Network error for chosen attributes of dataset";
    }
    
    
    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    @Override
    public Point2D[] getData(Attribute attribute1, Attribute attribute2) {
        return null;
    }
}
