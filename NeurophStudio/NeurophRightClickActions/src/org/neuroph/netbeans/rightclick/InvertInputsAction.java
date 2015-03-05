/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.rightclick.InverseInputsAction"
)
@ActionRegistration(
        displayName = "#CTL_InverseInputsAction"
)
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 172)
@Messages("CTL_InverseInputsAction=Inverse Inputs")
public final class InvertInputsAction implements ActionListener {

    private final DataSetDataObject context;

    public InvertInputsAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        
        DataSet dataSet = context.getDataSet();
        InvertInputs inverter = new InvertInputs();
        inverter.invert(dataSet);

    }
    
    public class InvertInputs {
        
        public DataSet invert(DataSet dataSet) {
            for (DataSetRow row : dataSet.getRows()) {
                double[] inputs = row.getInput();
                for (int i = 0; i < inputs.length; i++) {
                   if (inputs[i]>=0)
                        inputs[i] = 1 - inputs[i];
                   else if (inputs[i]<0)
                       inputs[i] = -1 - inputs[i];
                }
            }   
            
            return dataSet;
        }
    }
    
    
    
    
}
