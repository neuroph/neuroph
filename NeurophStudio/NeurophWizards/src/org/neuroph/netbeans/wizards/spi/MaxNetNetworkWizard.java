package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.MaxnetVisualPanel1;
import org.neuroph.nnet.MaxNet;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class MaxNetNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return MaxnetVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");

        int maxnetNeuronsNum = Integer.parseInt((String) settings.getProperty("neurons number"));
        MaxNet nnet8 = NeuralNetworkFactory.createMaxNet(maxnetNeuronsNum);
        nnet8.setLabel(neuralNetworkName);

        MaxnetVisualPanel1.getInstance().clearForm();
        return nnet8;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("neurons number", ((MaxnetVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int maxnetNeuronNumber = Integer.parseInt(((MaxnetVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Neurons number must integer value!", null);
        }
    }

}
