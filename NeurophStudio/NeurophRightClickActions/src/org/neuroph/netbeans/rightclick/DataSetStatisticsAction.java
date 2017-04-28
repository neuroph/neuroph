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
        
        DataSet dataSet = context.getDataSet();
        DataSetStatistics statistics = new DataSetStatistics(dataSet);
        statistics.calculateStatistics();
        this.outputStatistics(statistics);
        
        DataSetStatTopComponent comp = new DataSetStatTopComponent();
        comp.open();
        comp.requestActive();
        comp.openChart(statistics);
    }
    
    private void outputStatistics(DataSetStatistics statistics) {
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Dataset statistics for " + statistics.getDataSet().getLabel() + " dataset");
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("MIN: " + Arrays.toString(statistics.getMin()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("MIN: " + Arrays.toString(statistics.getMin()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("MAX: " + Arrays.toString(statistics.getMax()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("MEAN: " + Arrays.toString(statistics.getMean()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("SUM: " + Arrays.toString(statistics.getSum()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("VAR: " + Arrays.toString(statistics.getVar()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("STDDEV: " + Arrays.toString(statistics.getStdDev()));
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("FREQ: " + Arrays.toString(statistics.getFrequency()));
    }
}
