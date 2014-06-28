/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.charts.providers3d;

import org.nugs.graph3d.api.DataProvider3D;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Vedrana Gajic
 */
public class OutputDataProvider3D implements DataProvider3D<Point3D> {

    DataSet dataSet;
    NeuralNetwork nnet;

    public OutputDataProvider3D(DataSet dataSet, NeuralNetwork nnet) {
        this.dataSet = dataSet;
        this.nnet = nnet;
    }

    @Override
    public String toString() {
        return "Network outputs for entire dataset";
    }

    @Override
    public Point3D[] getData(Attribute ...attr) {
        int dataSetRowCount = dataSet.getRows().size();
        int neuronsCount = nnet.getLayerAt(attr[0].getIndex()).getNeuronsCount();

        Point3D[] output = new Point3D[(dataSetRowCount * neuronsCount) + 1];
        int counter = 1;

        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            nnet.setInput(rows.get(i).getInput());
            nnet.calculate();
            Neuron[] neurons = nnet.getLayerAt(attr[0].getIndex()).getNeurons();
            for (int j = 0; j < neurons.length; j++) {
                double out = neurons[j].getOutput();
                output[counter] = new Point3D(i + 1, j + 1, out);
                counter++;
            }
        }

        return output;
    }

}
