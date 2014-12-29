/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.SupervisedHebbianVisualPanel1;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class SupervisedHebianNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return SupervisedHebbianVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int supervisedHebbianInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int supervisedHebbianOutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        TransferFunctionType supervisedHebbianTransferFunction = TransferFunctionType.valueOf((String) settings.getProperty("transfer function"));
        SupervisedHebbianNetwork nnet6 = NeuralNetworkFactory.createSupervisedHebbian(supervisedHebbianInputNeurons, supervisedHebbianOutputNeurons, supervisedHebbianTransferFunction);
        nnet6.setLabel(neuralNetworkName);
        SupervisedHebbianVisualPanel1.getInstance().clearForm();

        return nnet6;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((SupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("output number", ((SupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("transfer function", ((SupervisedHebbianVisualPanel1) getComponent()).getTransferFunctionComboBox().getSelectedItem().toString().toUpperCase());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int supervisedHebbianInputsNumber = Integer.parseInt(((SupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int supervisedHebbianOutputsNumber = Integer.parseInt(((SupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
