package org.neuroph.netbeans.classifier.eval;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "Tools",
        id = "org.neuroph.netbeans.classifier.eval.RocAction"
)
@ActionRegistration(
        displayName = "#CTL_RocAction2"
)
@ActionReference(path = "Menu/Tools", position = 52)
@Messages("CTL_RocAction2=ROC Curve")
public final class RocAction implements ActionListener {

    private final NeuralNetAndDataSet context;

    public RocAction(NeuralNetAndDataSet context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {             
       RocTopComponent tc = new RocTopComponent();
       tc.setNeuralNetworkAndDataSet(context);
       tc.open();
       tc.requestActive();
       
       // should I set NeuralNetAndDataSet from here or look in th elookup?
       // IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Testing neural network "+trainingController.getNetwork().getLabel() +" for data set "+trainingController.getDataSet().getLabel());    
    }
}
