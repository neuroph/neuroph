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
public class ErrorDataProvider2D implements DataProvider2D {

    DataSet dataSet;
    NeuralNetwork nnet;
    Point2D[] error;

    public ErrorDataProvider2D() {
    }

    public ErrorDataProvider2D(DataSet dataSet, NeuralNetwork nnet) {
        this.dataSet = dataSet;
        this.nnet = nnet;
    }

    @Override
    public Point2D[] getData() {
        int numberOfPoints = dataSet.getRows().size();
        error = new Point2D.Double[numberOfPoints + 1];
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            DataSetRow row = rows.get(i);
            nnet.setInput(row.getInput());
            nnet.calculate();
            Neuron outputNeuron = nnet.getOutputNeurons()[0];
            double err = row.getDesiredOutput()[0] - outputNeuron.getOutput();
            error[i + 1] = new Point2D.Double(i + 1, err);
        }
        return error;
    }

    @Override
    public String getXLabel() {
        return "Dataset row";
    }
     @Override
    public String getYLabel() {
        return "Error";
    }

    @Override
    public String toString() {
        return "Network errors for entire dataset";
    }

    @Override
    public Point2D[] getData(Attribute attribute1, Attribute attribute2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
