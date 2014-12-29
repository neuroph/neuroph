package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.netbeans.wizards.MultiLayerPerceptronVisualPanel1;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class MultilayerPerceptronNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return MultiLayerPerceptronVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends SupervisedLearning> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        String inputNeuros = (String) settings.getProperty("inputs number");
        String hiddenNeuros = (String) settings.getProperty("hidden number");
        String outputNeuros = (String) settings.getProperty("outputs number");
        String neurosNum = inputNeuros + " " + hiddenNeuros + " " + outputNeuros;

        String useBiasS = settings.getProperty("use bias").toString();
        String connectI2OS = settings.getProperty("connect in2out").toString();
        boolean useBias = false;
        boolean connectI2O = false;
        if (useBiasS.equalsIgnoreCase("true")) {
            useBias = true;
        }
        if (connectI2OS.equalsIgnoreCase("true")) {
            connectI2O = true;
        }

        String transferFunctionS = (String) settings.getProperty("transfer function");
        TransferFunctionType transferFunction = TransferFunctionType.valueOf(transferFunctionS);

        String learningRuleS = (String) settings.getProperty("learning rule");
        MultiLayerPerceptron nnet2 = null;
        if (learningRuleS.equalsIgnoreCase("backpropagation")) {
            nnet2 = NeuralNetworkFactory.createMLPerceptron(
                    neurosNum, transferFunction, BackPropagation.class, useBias, connectI2O);
        }
        if (learningRuleS.equalsIgnoreCase("backpropagation with momentum")) {
            nnet2 = NeuralNetworkFactory.createMLPerceptron(
                    neurosNum, transferFunction, MomentumBackpropagation.class, useBias, connectI2O);
        }
        if (learningRuleS.equalsIgnoreCase("resilient propagation")) {
            nnet2 = NeuralNetworkFactory.createMLPerceptron(
                    neurosNum, transferFunction, ResilientPropagation.class, useBias, connectI2O);
        }
        if (learningRuleS.equalsIgnoreCase("dynamic backpropagation")) {
            nnet2 = NeuralNetworkFactory.createMLPerceptron(
                    neurosNum, transferFunction, DynamicBackPropagation.class, useBias, connectI2O);
        }
        nnet2.setLabel(neuralNetworkName);
        MultiLayerPerceptronVisualPanel1.getInstance().clearForm();
        return nnet2;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("inputs number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getInputNeurosField().getText().trim());
        ((WizardDescriptor) settings).putProperty("hidden number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getHiddenNeurosField().getText().trim());
        ((WizardDescriptor) settings).putProperty("outputs number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getOutputNeurosField().getText().trim());
        ((WizardDescriptor) settings).putProperty("transfer function", ((MultiLayerPerceptronVisualPanel1) getComponent()).getTransferFuntcionComboBox().getSelectedItem().toString().toUpperCase());
        ((WizardDescriptor) settings).putProperty("learning rule", ((MultiLayerPerceptronVisualPanel1) getComponent()).getLearningRuleComboBox().getSelectedItem());
        ((WizardDescriptor) settings).putProperty("use bias", ((MultiLayerPerceptronVisualPanel1) getComponent()).getUseBiasNeurosCheckBox().isSelected());
        ((WizardDescriptor) settings).putProperty("connect in2out", ((MultiLayerPerceptronVisualPanel1) getComponent()).getConnectInputToOutputCheckBox().isSelected());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int multiLayerPerceptronInputsNumber = Integer.parseInt(((MultiLayerPerceptronVisualPanel1) getComponent()).getInputNeurosField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        String[] hiddenNeurons = ((MultiLayerPerceptronVisualPanel1) getComponent()).getHiddenNeurosField().getText().trim().split(" ");
        for (int i = 0; i < hiddenNeurons.length; i++) {
            try {
                int multiLayerPerceptronHiddenNumber = Integer.parseInt(hiddenNeurons[i]);
            } catch (NumberFormatException ex) {
                throw new WizardValidationException(null, "Hidden numbers must integer value seperated with space!", null);
            }
        }
        try {
            int mutliLayerPerceptronOutputsNumber = Integer.parseInt(((MultiLayerPerceptronVisualPanel1) getComponent()).getOutputNeurosField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
