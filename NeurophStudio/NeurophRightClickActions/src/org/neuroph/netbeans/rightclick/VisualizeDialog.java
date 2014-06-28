/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.charts.NeuralNetVisualizationTopComponent;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.VisualizeDialog")
@ActionRegistration(
    displayName = "#CTL_VisualizeDialog")
@Messages("CTL_VisualizeDialog=Visualize dialog")
public final class VisualizeDialog extends AbstractAction implements ActionListener {

    private NeuralNetworkDataObject context;

    public VisualizeDialog(NeuralNetworkDataObject context) {
        super("Network out/err for dataset");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        NeuralNetVisualizationTopComponent comp = new NeuralNetVisualizationTopComponent();
        comp.open();
        comp.requestActive();
        comp.orderForm(context.getNeuralNetwork());
        
    }
}
