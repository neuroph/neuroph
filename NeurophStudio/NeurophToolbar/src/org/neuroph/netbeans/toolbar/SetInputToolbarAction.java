/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTraining;
import org.neuroph.netbeans.main.easyneurons.dialog.SetNetworkInputDialog;
import org.neuroph.netbeans.visual.GraphViewTopComponent;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "org.neuroph.netbeans.toolbar.SetInputToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/InputNeuron.png",
        displayName = "#CTL_SetInputToolbarAction")
@ActionReference(path = "Toolbars/File", position = -400)
@Messages("CTL_SetInputToolbarAction=Set input")
public final class SetInputToolbarAction implements ActionListener {

    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Lookup global = Utilities.actionsGlobalContext();
        NeuralNetwork nnet = global.lookup(NeuralNetwork.class);
        if (nnet == null) {
            JOptionPane.showMessageDialog(null, "Neural network is not selected!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SetNetworkInputDialog dialog = new SetNetworkInputDialog(null, true,
                new NeuralNetworkTraining(nnet));
        dialog.setVisible(true);

        TopComponent graph = TopComponent.getRegistry().getActivated();
        ((GraphViewTopComponent) graph).refresh();

    }
}
