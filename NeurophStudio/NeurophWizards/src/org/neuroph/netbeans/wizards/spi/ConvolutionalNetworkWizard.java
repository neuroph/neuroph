package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import java.util.List;
import javax.swing.JPanel;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.ConvolutionalLayerPanel;
import org.neuroph.netbeans.wizards.ConvolutionalNetworkVisualPanel1;
import org.neuroph.netbeans.wizards.ConvolutionalNetworkVisualPanel2;
import org.neuroph.netbeans.wizards.PoolingLayerPanel;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class ConvolutionalNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return ConvolutionalNetworkVisualPanel2.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        String outputNeuronCount = (String) settings.getProperty("outputNeuronCount");
        String inputWidth = (String) settings.getProperty("inputWidth");
        String inputHeight = (String) settings.getProperty("inputHeight");
        List<JPanel> panelList = (List<JPanel>) settings.getProperty("layers");

        Layer2D.Dimensions inputDimension = new Layer2D.Dimensions(Integer.parseInt(inputWidth), Integer.parseInt(inputHeight));
        ConvolutionalNetwork convolutionNetwork;

        ConvolutionalNetwork.ConvolutionalNetworkBuilder cnnBuilder = new ConvolutionalNetwork.ConvolutionalNetworkBuilder(inputDimension, 1);

        for (JPanel panel : panelList) {
            if (panel.getToolTipText().equals("clp")) {
                ConvolutionalLayerPanel clp = (ConvolutionalLayerPanel) panel;
                int nuberOfMaps = Integer.parseInt(clp.getJtxtNumberOfMaps().getText().trim());
                Kernel k = new Kernel(Integer.parseInt(clp.getJtxtKernelWidth().getText().trim()), Integer.parseInt(clp.getJtxtKernelheight().getText().trim()));
                cnnBuilder = cnnBuilder.withConvolutionLayer(k, nuberOfMaps);

            } else {
                PoolingLayerPanel clp = (PoolingLayerPanel) panel;
                int nuberOfMaps = Integer.parseInt(clp.getJtxtNumberOfMaps().getText().trim());
                Kernel k = new Kernel(Integer.parseInt(clp.getJtxtKernelWidth().getText().trim()), Integer.parseInt(clp.getJtxtKernelheight().getText().trim()));
                cnnBuilder = cnnBuilder.withConvolutionLayer(k, nuberOfMaps);
            }
        }
        int outputNeouron = Integer.parseInt(outputNeuronCount);
        cnnBuilder = cnnBuilder.withFullConnectedLayer(outputNeouron);
        convolutionNetwork = cnnBuilder.createNetwork();
        convolutionNetwork.setLabel(neuralNetworkName);
        ConvolutionalNetworkVisualPanel2.getInstance().clearForm();
        return convolutionNetwork;
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("outputNeuronCount", ((ConvolutionalNetworkVisualPanel2) getComponent()).getJtxtNumberOfOutputNeurons().getText().trim());
        ((WizardDescriptor) settings).putProperty("inputHeight", ((ConvolutionalNetworkVisualPanel2) getComponent()).getInputHeight().getText().trim());
        ((WizardDescriptor) settings).putProperty("inputWidth", ((ConvolutionalNetworkVisualPanel2) getComponent()).getInputWidth().getText().trim());
        ((WizardDescriptor) settings).putProperty("layers", ((ConvolutionalNetworkVisualPanel2) getComponent()).getPanelList());

    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        //
    }
}
