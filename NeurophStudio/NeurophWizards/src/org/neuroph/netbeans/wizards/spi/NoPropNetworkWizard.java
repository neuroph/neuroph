package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.dev.noprop.NoPropNet;
import org.neuroph.netbeans.wizards.NoPropVisualPanel1;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.RangeRandomizer;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class NoPropNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return NoPropVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        int noPropInputNeurons = Integer.parseInt((String) settings.getProperty("inputNeurons"));
        int noPropHiddenNeurons = Integer.parseInt((String) settings.getProperty("hiddenNeurons"));
        int noPropOutputNeurons = Integer.parseInt((String) settings.getProperty("outputNeurons"));

        int[] neuronsCount = new int[3];
        neuronsCount[0] = noPropInputNeurons;
        neuronsCount[1] = noPropHiddenNeurons;
        neuronsCount[2] = noPropOutputNeurons;

        NoPropNet noPropNet = new NoPropNet(TransferFunctionType.TANH, true, neuronsCount);
        noPropNet.randomizeWeights(new RangeRandomizer(-1, 1));

        noPropNet.setLabel(neuralNetworkName);

        NoPropVisualPanel1.getInstance().clearForm();
        return noPropNet;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("inputNeurons", ((NoPropVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
        ((WizardDescriptor) settings).putProperty("outputNeurons", ((NoPropVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
        ((WizardDescriptor) settings).putProperty("hiddenNeurons", ((NoPropVisualPanel1) getComponent()).getHiddenNeuronsField().getText().trim());
    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
