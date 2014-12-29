package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.UnsupervisedHebbianVisualPanel1;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class UnsupervisedHebianNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return UnsupervisedHebbianVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int unsupervisedHebbianInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int unsupervisedHebbianOutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        TransferFunctionType unsupervisedHebbianTransferFunction = TransferFunctionType.valueOf((String) settings.getProperty("transfer function"));
        UnsupervisedHebbianNetwork nnet7 = NeuralNetworkFactory.createUnsupervisedHebbian(unsupervisedHebbianInputNeurons, unsupervisedHebbianOutputNeurons, unsupervisedHebbianTransferFunction);
        nnet7.setLabel(neuralNetworkName);

        UnsupervisedHebbianVisualPanel1.getInstance().clearForm();
        return nnet7;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((UnsupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("output number", ((UnsupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("transfer function", ((UnsupervisedHebbianVisualPanel1) getComponent()).getTransferFunctionComboBox().getSelectedItem().toString().toUpperCase());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int unsupervisedHebbianInputsNumber = Integer.parseInt(((UnsupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int unsupervisedHebbianOutputsNumber = Integer.parseInt(((UnsupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
