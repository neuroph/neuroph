package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.RBFVisualPanel1;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class RBFNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return RBFVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int rbfinputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int rbfNeurons = Integer.parseInt((String) settings.getProperty("rbf number"));
        int rbfoutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        RBFNetwork nnet10 = NeuralNetworkFactory.createRbfNetwork(rbfinputNeurons, rbfNeurons, rbfoutputNeurons);
        nnet10.setLabel(neuralNetworkName);

        RBFVisualPanel1.getInstance().clearForm();

        return nnet10;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((RBFVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("rbf number", ((RBFVisualPanel1) getComponent()).getRbfNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("output number", ((RBFVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int rbfInputsNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int rbfNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getRbfNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "RBF number must integer value!", null);
        }
        try {
            int rbfOutputsNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
