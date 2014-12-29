/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.KohonenVisualPanel1;
import org.neuroph.nnet.Kohonen;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class KohenNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return KohonenVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int kohonenInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int kohonenMapNeurons = Integer.parseInt((String) settings.getProperty("map number"));
        Kohonen nnet5 = NeuralNetworkFactory.createKohonen(kohonenInputNeurons, kohonenMapNeurons);
        nnet5.setLabel(neuralNetworkName);

        KohonenVisualPanel1.getInstance().clearForm();
        return nnet5;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((KohonenVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
        ((WizardDescriptor) settings).putProperty("map number", ((KohonenVisualPanel1) getComponent()).getMapNeuronsField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int kohonenInputsNumber = Integer.parseInt(((KohonenVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int kohonenMapNumber = Integer.parseInt(((KohonenVisualPanel1) getComponent()).getMapNeuronsField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Map number must integer value!", null);
        }
    }

}
