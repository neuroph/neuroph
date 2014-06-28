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
id = "RightClickActions.ShuffleTrainingSetAction")
@ActionRegistration(iconBase = "org/neuroph/netbeans/rightclick/shuffle.png",
        displayName = "#CTL_ShuffleTrainingSetAction")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-tset/Actions", position = 200)
})
@Messages("CTL_ShuffleTrainingSetAction=Shuffle")
public final class ShuffleTrainingSetAction implements ActionListener {

    private final DataSetDataObject context;

    public ShuffleTrainingSetAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        System.out.println(context.toString());
        context.getDataSet().shuffle();
    }
}
