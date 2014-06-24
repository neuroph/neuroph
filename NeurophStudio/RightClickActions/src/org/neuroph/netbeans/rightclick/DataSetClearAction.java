/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "BpelNodes",
        id = "IDEActions.DataSetClearAction")
@ActionRegistration(
        displayName = "#CTL_DataSetClearAction")
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 200)
@Messages("CTL_DataSetClearAction=Clear")
public final class DataSetClearAction implements ActionListener {

    private final DataSetDataObject context;

    public DataSetClearAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {        
        if (JOptionPane.showConfirmDialog(null, "This will clear entire dats set", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION ) {
          context.getDataSet().clear();
        }
    }
}
