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
        int dataSetRowCount = dataSet.getRows().size();
        int neuronsCount = nnet.getLayerAt(attribute[0].getIndex()).getNeuronsCount();
        Point3D[] outputPoints3D = new Point3D[(dataSetRowCount * neuronsCount)+1];// +1
        int counter = 1;//1
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < dataSetRowCount; i++) { // iterate all rows from data set
            DataSetRow row = rows.get(i);
            nnet.setInput(row.getInput());  // set current row as network input and
            nnet.calculate();               // calculate network
            Neuron[] neurons = nnet.getLayerAt(attribute[0].getIndex()).getNeurons(); // get neurons at specified layer
            for (int j = 0; j < neurons.length; j++) {
                double out = neurons[j].getOutput();
                double colVal = row.getInput()[attribute[1].getIndex() - 1];
                outputPoints3D[counter] = new Point3D(colVal, j, out);
                counter++;
            }
        }

        return outputPoints3D;
    }

}
