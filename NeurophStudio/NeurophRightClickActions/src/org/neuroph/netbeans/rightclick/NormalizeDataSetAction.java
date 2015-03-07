/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.rightclick.NormalizeDataSetAction"
)
@ActionRegistration(
        displayName = "#CTL_NormalizeDataSetAction"
)
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 140)
@Messages("CTL_NormalizeDataSetAction=Normalize")
public final class NormalizeDataSetAction extends AbstractAction implements ActionListener, Presenter.Popup   {

    private DataSetDataObject context;

    public NormalizeDataSetAction(DataSetDataObject context) {
        this.context = context;
    }

    public NormalizeDataSetAction() {
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ev) {

    }
    
    @Override
    public JMenuItem getPopupPresenter() {
        Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
        context = actionsGlobalContext.lookup(DataSetDataObject.class);        
        
        JMenu result = new JMenu("Normalize");

        result.add(new JMenuItem(new MaxNormalizationAction(context)));
        result.add(new JMenuItem(new MaxMinNormalizationAction(context)));
        result.add(new JMenuItem(new DecimalScaleNormalizationAction(context)));
        result.add(new JMenuItem(new RangeNormalizationAction(context)));

        return result;
    }    
    
    
}
/*


*/