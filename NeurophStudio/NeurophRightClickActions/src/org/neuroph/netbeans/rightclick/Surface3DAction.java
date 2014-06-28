package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.charts.SurfaceTopComponent;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.Surface3DAction")
@ActionRegistration(
    displayName = "#CTL_Surface3DAction")
@Messages("CTL_Surface3DAction=Surface 3D")
public final class Surface3DAction extends AbstractAction implements ActionListener {

    private NeuralNetworkDataObject context;

    public Surface3DAction(NeuralNetworkDataObject context) {
        super("Surface 3D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        SurfaceTopComponent.getInstance().open();
        SurfaceTopComponent.getInstance().requestActive();
        SurfaceTopComponent.getInstance().openChart(context.getNeuralNetwork());
    }
}
