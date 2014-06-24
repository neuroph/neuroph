package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "IDEActions.NormalizationTrainingSetAction")
@ActionRegistration(iconBase = "org/neuroph/netbeans/rightclick/normalize.png",
        displayName = "#CTL_NormalizationTrainingSetAction")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-tset/Actions", position = 150)
})
@Messages("CTL_NormalizationTrainingSetAction=Normalize")
public final class NormalizationTrainingSetAction implements ActionListener {

    private final DataSetDataObject context;

    public NormalizationTrainingSetAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // open normalization doalog and choose normalization method and params
     //   context.getDataSet().normalize();
    }
}
