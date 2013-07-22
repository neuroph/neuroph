/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.charts.providers3d;

import org.nugs.graph3d.api.DataProvider3D;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetOutputDataProvider3D implements DataProvider3D {

    DataSet dataSet;
    NeuralNetwork nnet;

    public DatasetOutputDataProvider3D(DataSet dataSet, NeuralNetwork nnet) {
        this.dataSet = dataSet;
        this.nnet = nnet;
    }

    @Override
    public String toString() {
        return "Network output for chosen attributes of dataset";
    }

    @Override
    public Point3D[] getData(Attribute ... attribute) {
        
        //attribute[0] = layer, attribute[1] = dataset attribute (input)
        int numberOfPoints = dataSet.getRows().size();
        int numberOfOutputNeurons = nnet.getLayerAt(attribute[0].getIndex()).getNeuronsCount();
        Point3D[] output = new Point3D[(numberOfPoints * numberOfOutputNeurons) + 1];
        int counter = 1;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            DataSetRow row = rows.get(i);
            nnet.setInput(row.getInput());
            nnet.calculate();
            Neuron[] outputNeurons = nnet.getLayerAt(attribute[1].getIndex()).getNeurons();
            for (int j = 0; j < outputNeurons.length; j++) {
                double out = outputNeurons[j].getOutput();
                double colVal = row.getInput()[attribute[1].getIndex() - 1];
                output[counter] = new Point3D(colVal, j + 1, out);
                counter++;
            }
        }

        return output;
    }

}
