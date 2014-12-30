package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.CompetitiveNetworkVisualPanel1;
import org.neuroph.nnet.CompetitiveNetwork;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class CompetitiveNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return CompetitiveNetworkVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int competitiveNetworkInputNeurons = Integer.parseInt((String) settings.getProperty("input number"));
        int competitiveNetworkOutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        CompetitiveNetwork nnet9 = NeuralNetworkFactory.createCompetitiveNetwork(competitiveNetworkInputNeurons, competitiveNetworkOutputNeurons);
        nnet9.setLabel(neuralNetworkName);

        CompetitiveNetworkVisualPanel1.getInstance().clearForm();

        return nnet9;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("input number", ((CompetitiveNetworkVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        ((WizardDescriptor) settings).putProperty("output number", ((CompetitiveNetworkVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int competitiveNetworkInputsNumber = Integer.parseInt(((CompetitiveNetworkVisualPanel1) getComponent()).getInputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Inputs number must integer value!", null);
        }
        try {
            int competitiveNetworkOutputsNumber = Integer.parseInt(((CompetitiveNetworkVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
