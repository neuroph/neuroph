/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.charts.providers;

import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.DataProvider3D;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Milos Randjic
 */
public class DatasetDataProvider3D implements DataProvider3D<Point3D.Float>{
    
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

    private float getValue(DataSetRow row, Attribute attr) {
        if (attr.isOutput()) {
            return (float) row.getDesiredOutput()[attr.getIndex()];
        }
        return (float) row.getInput()[attr.getIndex()];
    }
    

    @Override
    public Point3D.Float[] getData(Attribute... attr) {
        int numberOfPoints = dataSet.getRows().size();

        Point3D.Float[] data = new Point3D.Float[numberOfPoints];
        int counter = 0;
        List<DataSetRow> rows = dataSet.getRows();
        for (int i = 0; i < rows.size(); i++) {

            float x = getValue(rows.get(i), attr[0]);
            float y = getValue(rows.get(i), attr[1]);
            float z = getValue(rows.get(i), attr[2]);
            
            data[counter] = new Point3D.Float(x, y, z);
            counter++;
        }

        return data;
    }
    
}
