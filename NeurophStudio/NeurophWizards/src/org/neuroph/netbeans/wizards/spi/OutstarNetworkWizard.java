package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.OutstarVisualPanel1;
import org.neuroph.nnet.Outstar;
import org.neuroph.util.NeuralNetworkFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class OutstarNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return OutstarVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int outstarOutputNeurons = Integer.parseInt((String) settings.getProperty("output number"));
        Outstar nnet12 = NeuralNetworkFactory.createOutstar(outstarOutputNeurons);
        nnet12.setLabel(neuralNetworkName);

        OutstarVisualPanel1.getInstance().clearForm();
        return nnet12;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("output number", ((OutstarVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        try {
            int instarOutputsNumber = Integer.parseInt(((OutstarVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Outputs number must integer value!", null);
        }
    }

}
