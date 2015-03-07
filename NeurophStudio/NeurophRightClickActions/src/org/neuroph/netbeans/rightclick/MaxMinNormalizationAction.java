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
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.rightclick.MaxMinNormalizationAction"
)
@ActionRegistration(
        displayName = "#CTL_MaxMinNormalizationAction"
)
@Messages("CTL_MaxMinNormalizationAction=Max Min")
public final class MaxMinNormalizationAction  extends AbstractAction implements ActionListener  {

    private final DataSetDataObject context;

    public MaxMinNormalizationAction(DataSetDataObject context) {
        super("Max Min");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
       DataSet dataSet = context.getDataSet();
       MaxMinNormalizer norm = new MaxMinNormalizer();
       norm.normalize(dataSet);
       
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Normalized data set " + dataSet.getLabel() + " using Max Min normalization method");
    }
}
