/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDEActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.charts.Histogram3DTopComponent;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.Hist3DAction")
@ActionRegistration(
    displayName = "#CTL_Hist3DAction")
@Messages("CTL_Hist3DAction=Histogram 3D")
public final class Hist3DAction extends AbstractAction implements ActionListener {

    private final NeuralNetworkDataObject context;

     public Hist3DAction(NeuralNetworkDataObject context) {
        super("Histogram 3D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Histogram3DTopComponent.getInstance().open();
        Histogram3DTopComponent.getInstance().requestActive();
        Histogram3DTopComponent.getInstance().openChart(context.getNeuralNetwork());
    }
}
