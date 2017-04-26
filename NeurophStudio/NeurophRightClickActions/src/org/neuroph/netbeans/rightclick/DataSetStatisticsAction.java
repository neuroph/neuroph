/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.neuroph.netbeans.main.DataSetStatTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.rightclick.DataSetStatisticsAction"
)
@ActionRegistration(
        displayName = "#CTL_DataSetStatisticsAction"
)
@ActionReference(path = "Loaders/Languages/Actions", position = 0)
@Messages("CTL_DataSetStatisticsAction=DataSet Statistics")
public final class DataSetStatisticsAction implements ActionListener {

    private final DataSetDataObject context;

    public DataSetStatisticsAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DataSetStatTopComponent comp = new DataSetStatTopComponent();
        comp.open();
        comp.requestActive();
        comp.openChart(context.getDataSet());
    }
}
