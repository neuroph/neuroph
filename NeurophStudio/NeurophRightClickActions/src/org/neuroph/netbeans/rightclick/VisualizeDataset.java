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
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
    category = "BpelNodes",
id = "IDEActions.VisualizeDataset")
@ActionRegistration(
    displayName = "#CTL_VisualizeDataset")
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 200, separatorBefore = 195)
@Messages("CTL_VisualizeDataset=Visualize")
public final class VisualizeDataset extends AbstractAction implements ActionListener, Presenter.Popup {

    private DataSetDataObject context;

    public VisualizeDataset(DataSetDataObject context) {
        this.context = context;
    }

    public VisualizeDataset() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
        context = actionsGlobalContext.lookup(DataSetDataObject.class);

        JMenu result = new JMenu("Visualize");

        result.add(new JMenuItem(new DatasetVisualization2DAction(context)));
        result.add(new JMenuItem(new DatasetVisualization3DAction(context)));
        return result;
    }
}
