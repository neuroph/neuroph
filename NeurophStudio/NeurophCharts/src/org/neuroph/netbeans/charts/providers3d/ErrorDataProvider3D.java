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
public class ErrorDataProvider3D implements DataProvider3D<Point3D> {

    DataSet dataSet;
    NeuralNetwork nnet;

//    public ErrorDataProvider3D() {
//    }

    public ErrorDataProvider3D(DataSet dataSet, NeuralNetwork nnet) {
        this.dataSet = dataSet;
        this.nnet = nnet;
    }

    @Override
    public Point3D[] getData(Attribute...attr) {
        int dataSetRowCount = dataSet.getRows().size();
        int neuronsCount = nnet.getOutputNeurons().length;

        Point3D[] errorPoints3D = new Point3D[(dataSetRowCount * neuronsCount) + 1];
        int counter = 1;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            nnet.setInput(rows.get(i).getInput());
            nnet.calculate();
            Neuron[] outputNeurons = nnet.getOutputNeurons();
            for (int j = 0; j < outputNeurons.length; j++) {
                double err = rows.get(i).getDesiredOutput()[j] - outputNeurons[j].getOutput();
                errorPoints3D[counter] = new Point3D(i + 1, j + 1, err);
                counter++;
            }
        }

        return errorPoints3D;
    }

   
}
