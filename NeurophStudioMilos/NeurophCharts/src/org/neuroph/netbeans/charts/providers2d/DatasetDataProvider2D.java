/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.charts.providers2d;

import org.nugs.graph2d.api.DataProvider2D;
import java.awt.geom.Point2D;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetDataProvider2D implements DataProvider2D {

    private DataSet dataSet;
    private Attribute attribute1;
    private Attribute attribute2;

    public DatasetDataProvider2D(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public Point2D[] getData(Attribute attribute1, Attribute attribute2) {
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;

        int numberOfPoints = dataSet.getRows().size();

        Point2D[] data = new Point2D[numberOfPoints];
        int counter = 0;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {
            double x = getValue(rows.get(i), attribute1);
            double y = getValue(rows.get(i), attribute2);
            data[counter] = new Point2D.Double(x, y);
            counter++;

        }

        return data;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    @Override
    public Point2D[] getData() {
        return null;
    }

    @Override
    public String getXLabel() {
        return attribute1.getLabel();
    }

    @Override
    public String getYLabel() {
        return attribute2.getLabel();
    }

    private double getValue(DataSetRow row, Attribute attr) {
        if (attr.isOutput()) {
            return row.getDesiredOutput()[attr.getIndex()];
        }
        return row.getInput()[attr.getIndex()];
    }
}
