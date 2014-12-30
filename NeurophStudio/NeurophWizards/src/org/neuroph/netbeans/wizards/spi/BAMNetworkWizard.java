/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.BAMVisualPanel1;
import org.neuroph.nnet.BAM;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class BAMNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return BAMVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int bamInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int bamOutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        BAM nnet4 = NeuralNetworkFactory.createBam(bamInputNeurons, bamOutputNeurons);
        nnet4.setLabel(neuralNetworkName);
        
        BAMVisualPanel1.getInstance().clearForm();
        
        return nnet4;
     }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((BAMVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
        ((WizardDescriptor) settings).putProperty("output number", ((BAMVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int bamInputsNumber = Integer.parseInt(((BAMVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int bamOutputsNumber = Integer.parseInt(((BAMVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
