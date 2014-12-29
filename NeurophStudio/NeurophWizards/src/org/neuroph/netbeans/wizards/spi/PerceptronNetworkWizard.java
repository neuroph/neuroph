package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.netbeans.wizards.PerceptronVisualPanel1;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class PerceptronNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
                return PerceptronVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends SupervisedLearning> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int inputNeurons = Integer.parseInt((String) settings.getProperty("inputs number"));
        int outputNeurons = Integer.parseInt((String) settings.getProperty("outputs number"));
        String learningRule = ((String) settings.getProperty("learning rule"));
        Perceptron nnet1;
        if (learningRule.equalsIgnoreCase("Perceptron Learning")) {
            nnet1 = NeuralNetworkFactory.createPerceptron(inputNeurons,
                    outputNeurons, TransferFunctionType.STEP, PerceptronLearning.class);
        } else {
            nnet1 = NeuralNetworkFactory.createPerceptron(inputNeurons,
                    outputNeurons, TransferFunctionType.STEP, BinaryDeltaRule.class);
        }
        nnet1.setLabel(neuralNetworkName);
        PerceptronVisualPanel1.getInstance().clearForm();
        return nnet1;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("inputs number", ((PerceptronVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("outputs number", ((PerceptronVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("learning rule", ((PerceptronVisualPanel1) getComponent()).getLearningRuleComboBox().getSelectedItem().toString().trim());
     }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int perceptronInputsNumber = Integer.parseInt(((PerceptronVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int perceptronOutputsNumber = Integer.parseInt(((PerceptronVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
