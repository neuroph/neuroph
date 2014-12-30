package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.HopfieldVisualPanel1;
import org.neuroph.nnet.Hopfield;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class HopfildNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return HopfieldVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int neuronNum = Integer.parseInt((String) settings.getProperty("neurons number"));
        Hopfield nnet3 = NeuralNetworkFactory.createHopfield(neuronNum);
        nnet3.setLabel(neuralNetworkName);
        HopfieldVisualPanel1.getInstance().clearForm();
        return nnet3;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("neurons number", ((HopfieldVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int hopfieldNeuronsNumber = Integer.parseInt(((HopfieldVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Neurons number must integer value!", null);
        }
    }

}
