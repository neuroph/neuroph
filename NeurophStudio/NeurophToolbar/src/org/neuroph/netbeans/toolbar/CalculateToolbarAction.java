package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTraining;
import org.neuroph.netbeans.visual.VisualEditorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.toolbar.CalculateToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/calculate.png",
        displayName = "#CTL_CalculateToolbarAction")
@ActionReference(path = "Toolbars/Neuroph", position = -400)
@Messages("CTL_CalculateToolbarAction=Calculate")
public final class CalculateToolbarAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Lookup global = Utilities.actionsGlobalContext();
        NeuralNetwork nnet = global.lookup(NeuralNetwork.class);
        if (nnet == null) {
            JOptionPane.showMessageDialog(null, "Neural network is not selected!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        NeuralNetworkTraining training = new NeuralNetworkTraining(nnet);
        training.calculate();

        TopComponent graph = TopComponent.getRegistry().getActivated();
      if (graph instanceof VisualEditorTopComponent) {
            ((VisualEditorTopComponent) graph).refresh();
        }
    }
}
