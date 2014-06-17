/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.charts.providers3d;

import org.nugs.graph3d.api.DataProvider3D;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetDataProvider3D implements DataProvider3D {

    private DataSet dataSet;
    private boolean[] isOutput;

    public DatasetDataProvider3D(DataSet dataSet) {
        this.dataSet = dataSet;
        isOutput = new boolean[3];
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public boolean[] getIsOutput() {
        return isOutput;
    }

    public void setIsOutput(boolean[] isOutput) {
        this.isOutput = isOutput;
    }

    private double getValue(DataSetRow row, Attribute attr) {
        if (attr.isOutput()) {
            return row.getDesiredOutput()[attr.getIndex()];
        }
        return row.getInput()[attr.getIndex()];
    }
   
    @Override
    public Point3D[] getData(Attribute ...attr) {
        int numberOfPoints = dataSet.getRows().size();

        Point3D[] data = new Point3D[numberOfPoints];
        int counter = 0;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {

            double x = getValue(rows.get(i), attr[0]);
            double y = getValue(rows.get(i), attr[1]);
            double z = getValue(rows.get(i), attr[2]);
            
            data[counter] = new Point3D(x, y, z);
            counter++;
        }

        return data;
    }

   
}
