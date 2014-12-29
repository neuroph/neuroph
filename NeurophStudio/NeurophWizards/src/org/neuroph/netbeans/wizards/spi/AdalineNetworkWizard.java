/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.netbeans.wizards.AdalineVisualPanel1;
import org.neuroph.nnet.Adaline;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class AdalineNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return AdalineVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends SupervisedLearning> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int inputsNumber = Integer.parseInt((String) settings.getProperty("inputsNumber"));

        Adaline nnet0 = NeuralNetworkFactory.createAdaline(inputsNumber);
        nnet0.setLabel(neuralNetworkName);
        AdalineVisualPanel1.getInstance().clearForm();
        
        return nnet0;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("inputsNumber", ((AdalineVisualPanel1) getComponent()).getInputsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int adalineInputNumber = Integer.parseInt(((AdalineVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Input number must integer value!", null);
        }
    }

}
