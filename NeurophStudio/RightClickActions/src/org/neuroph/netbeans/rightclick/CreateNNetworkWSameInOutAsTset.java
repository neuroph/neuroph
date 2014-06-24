package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.neuroph.netbeans.wizards.*;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "RightClickActions.CreateNNetworkWSameInOutAsTset")
@ActionRegistration(iconBase = "org/neuroph/netbeans/rightclick/new.png",
displayName = "#CTL_CreateNNetworkWSameInOutAsTset")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-tset/Actions", position = 450)
})
@Messages("CTL_CreateNNetworkWSameInOutAsTset=Create Neural Network From Training Set")
public final class CreateNNetworkWSameInOutAsTset implements ActionListener {

    private final DataSetDataObject context;

    public CreateNNetworkWSameInOutAsTset(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        startWizardIterator();
    }

    private void startWizardIterator() {
        DataSet dataSet = context.getDataSet();
        
        String input = String.valueOf(dataSet.getInputSize());
        String output = String.valueOf(dataSet.getOutputSize());        

        NewNeuralNetworkWizardIterator neu = new NewNeuralNetworkWizardIterator();
        WizardDescriptor wiz = new WizardDescriptor(neu);
        neu.initialize(wiz);
        setInputOutputNumbers(input, output);
        neu.getWizard().setTitleFormat(new MessageFormat("{0}"));
        neu.getWizard().setTitle("Create Neural Network");
        if (DialogDisplayer.getDefault().notify(neu.getWizard()) == WizardDescriptor.FINISH_OPTION) {
            try {
                neu.instantiate();
            } catch (Exception ex) {
                ex.getMessage();
            }
            setInputOutputNumbers("", "");
        }
    }

    private void setInputOutputNumbers(String input, String output) {
        try {

            AdalineVisualPanel1.getInstance().getInputsNumField().setText(input);
            PerceptronVisualPanel1.getInstance().getInputsNumField().setText(input);
            PerceptronVisualPanel1.getInstance().getOutputsNumField().setText(output);
            MultiLayerPerceptronVisualPanel1.getInstance().getInputNeurosField().setText(input);
            MultiLayerPerceptronVisualPanel1.getInstance().getOutputNeurosField().setText(output);
            BAMVisualPanel1.getInstance().getInputNeuronsField().setText(input);
            BAMVisualPanel1.getInstance().getOutputNeuronsField().setText(output);
            KohonenVisualPanel1.getInstance().getInputNeuronsField().setText(input);
            SupervisedHebbianVisualPanel1.getInstance().getInputsNumField().setText(input);
            SupervisedHebbianVisualPanel1.getInstance().getOutputsNumField().setText(output);
            UnsupervisedHebbianVisualPanel1.getInstance().getInputsNumField().setText(input);
            UnsupervisedHebbianVisualPanel1.getInstance().getOutputsNumField().setText(output);
            CompetitiveNetworkVisualPanel1.getInstance().getInputsNumField().setText(input);
            CompetitiveNetworkVisualPanel1.getInstance().getOutputsNumField().setText(output);
            RBFVisualPanel1.getInstance().getInputsNumField().setText(input);
            RBFVisualPanel1.getInstance().getOutputsNumField().setText(output);
            InstarVisualPanel1.getInstance().getInputsNumField().setText(input);
            OutstarVisualPanel1.getInstance().getOutputsNumField().setText(output);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}