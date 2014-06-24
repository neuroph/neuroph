/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.neuroph.netbeans.files.nnet.NeuralNetworkDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.VisualizeAction")
@ActionRegistration(
    displayName = "#CTL_VisualizeAction")
@ActionReference(path = "Loaders/text/x-nnet/Actions", position = 1300, separatorAfter = 1350)
@Messages("CTL_VisualizeAction=Visualize")
public final class VisualizeAction extends AbstractAction implements ActionListener, Presenter.Popup {

    private NeuralNetworkDataObject context;

    public VisualizeAction(NeuralNetworkDataObject context) {
        this.context = context;
    }

    public VisualizeAction() {
        
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
   
    }

    @Override
    public JMenuItem getPopupPresenter() {
        Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
        context = actionsGlobalContext.lookup(NeuralNetworkDataObject.class);
   
        JMenu result = new JMenu("Visualize");
        result.add(new JMenuItem(new Hist2DAction(context)));
        result.add(new JMenuItem(new Hist3DAction(context)));
        result.add(new JMenuItem(new Surface3DAction(context)));
        result.add(new JMenuItem(new VisualizeDialog(context)));
       // result.add(new JMenuItem(new DatasetVisualization(context)));
        return result;
    }
}
