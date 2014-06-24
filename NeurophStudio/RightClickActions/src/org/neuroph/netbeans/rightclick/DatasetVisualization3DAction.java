package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.charts.VisualizeDataset3DTopComponent;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.DatasetVisualization3DAction")
@ActionRegistration(
    displayName = "#CTL_DatasetVisualization3DAction")
@Messages("CTL_DatasetVisualization3DAction=Dataset visualization in 3D")
public final class DatasetVisualization3DAction extends AbstractAction implements ActionListener {

    private DataSetDataObject context;

    public DatasetVisualization3DAction(DataSetDataObject context) {
        super("Dataset visualization in 3D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        VisualizeDataset3DTopComponent comp = new VisualizeDataset3DTopComponent();
        comp.open();
        comp.requestActive();
        comp.openChart(context.getDataSet());

    }
}
