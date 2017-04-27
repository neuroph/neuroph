/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.neuroph.netbeans.main.DataSetStatTopComponent;
import org.neuroph.util.DataSetStatistics;
import org.neuroph.util.DataSetColumnType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.rightclick.DataSetStatisticsAction"
)
@ActionRegistration(
        displayName = "#CTL_DataSetStatisticsAction"
)
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 200)
@Messages("CTL_DataSetStatisticsAction=Dataset statistics")
public final class DataSetStatisticsAction implements ActionListener {

    private final DataSetDataObject context;

    public DataSetStatisticsAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DataSetStatTopComponent comp = new DataSetStatTopComponent();
        DataSet dataSet = context.getDataSet();
        DataSetStatistics statistics = new DataSetStatistics(dataSet);
        statistics.setColumnType(4, DataSetColumnType.NOMINAL);
        statistics.setColumnType(5, DataSetColumnType.NOMINAL);
        statistics.setColumnType(6, DataSetColumnType.NOMINAL);
        statistics.calculateStatistics();
        System.out.println(Arrays.toString(statistics.getMin()));
//        comp.open();
//        comp.requestActive();
//        comp.openChart(dataSet);
        
        
        
//        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Normalized data set " + dataSet.getLabel() + " using Max normalization method");
    }
}
