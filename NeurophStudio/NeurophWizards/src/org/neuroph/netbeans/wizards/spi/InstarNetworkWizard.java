package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.InstarVisualPanel1;
import org.neuroph.nnet.Instar;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class InstarNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return InstarVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int instarInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        Instar nnet11 = NeuralNetworkFactory.createInstar(instarInputNeurons);
        nnet11.setLabel(neuralNetworkName);

        InstarVisualPanel1.getInstance().clearForm();
        return nnet11;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((InstarVisualPanel1) getComponent()).getInputsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int instarInputsNumber = Integer.parseInt(((InstarVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
    }

}
