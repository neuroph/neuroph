/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDEActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.nugs.neurophgraph3d.SurfaceTopComponent;
import org.nugs.neurophgraph3d.WireframeSurface;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.WireframeSurfaceAction")
@ActionRegistration(
    displayName = "#CTL_WireframeSurfaceAction")
@Messages("CTL_WireframeSurfaceAction=Wireframe surface")
public final class WireframeSurfaceAction extends AbstractAction implements ActionListener {

    private NeuralNetworkDataObject context;

    public WireframeSurfaceAction(NeuralNetworkDataObject context) {
        super("Surface 3D");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
//        WireframeSurface surface = new WireframeSurface();
//        surface.create(context.getNeuralNetwork());
        SurfaceTopComponent.getInstance().open();
        SurfaceTopComponent.getInstance().requestActive();
        SurfaceTopComponent.getInstance().openChart(context.getNeuralNetwork());
    }
}
