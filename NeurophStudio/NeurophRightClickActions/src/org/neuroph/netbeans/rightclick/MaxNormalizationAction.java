/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.rightclick.MaxNormalizationAction"
)
@ActionRegistration(
        displayName = "#CTL_MaxNormalizationAction"
)

@Messages("CTL_MaxNormalizationAction=Max")
public final class MaxNormalizationAction extends AbstractAction implements ActionListener  {

    private final DataSetDataObject context;

    public MaxNormalizationAction(DataSetDataObject context) {
        super("Max");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DataSet dataSet = context.getDataSet();
       MaxNormalizer norm = new MaxNormalizer();
       norm.normalize(dataSet);
       
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Normalized data set " + dataSet.getLabel() + " using Max normalization method");
    }
}
