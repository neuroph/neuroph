/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.neuroph.netbeans.main.easyneurons.HistogramChartTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.Hist2DAction")
@ActionRegistration(
    displayName = "#CTL_Hist2DAction")
@Messages("CTL_Hist2DAction=Weights Histogram 2D")
public final class Hist2DAction extends AbstractAction implements ActionListener {

    private final NeuralNetworkDataObject context;

    public Hist2DAction(NeuralNetworkDataObject context) {
        super("Weights Histogram 2D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        HistogramChartTopComponent.getDefault().open();
        HistogramChartTopComponent.getDefault().requestActive();
        HistogramChartTopComponent.getDefault().populateHistBins(context.getNeuralNetwork());
    }
}
