package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.charts.VisualizeDataset2DTopComponent;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.DatasetVisualization2DAction")
@ActionRegistration(
    displayName = "#CTL_DatasetVisualization2DAction")
@Messages("CTL_DatasetVisualization2DAction=Dataset visualization in 2D")
public final class DatasetVisualization2DAction extends AbstractAction implements ActionListener {

    private DataSetDataObject context;

    public DatasetVisualization2DAction(DataSetDataObject context) {
        super("Dataset visualization in 2D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        VisualizeDataset2DTopComponent comp = new VisualizeDataset2DTopComponent();
        comp.open();
        comp.requestActive();
        comp.openChart(context.getDataSet());

    }
}
