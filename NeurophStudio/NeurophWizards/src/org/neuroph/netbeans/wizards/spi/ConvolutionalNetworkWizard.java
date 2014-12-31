package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.wizards.ConvolutionalNetworkVisualPanel1;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

public class ConvolutionalNetworkWizard implements NetworkWizard {

    @Override
    public Component getComponent() {
        return ConvolutionalNetworkVisualPanel1.getInstance();
    }

    @Override
    public NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings) {
        String neuralNetworkName = (String) settings.getProperty("neural network name");
        String kernelWidth = (String) settings.getProperty("kernelWidth");
        String kernelHeight = (String) settings.getProperty("kernelHeight");
        String inputWidth = (String) settings.getProperty("inputWidth");
        String inputHeight = (String) settings.getProperty("inputHeight");
        String outputNeuronCount = (String) settings.getProperty("outputNeuronCount");
        String[] numberOfMaps = ((String) settings.getProperty("numberOfMaps")).split(" ");
        

        Layer2D.Dimensions inputDimension = new Layer2D.Dimensions(Integer.parseInt(inputWidth), Integer.parseInt(inputHeight));
        Kernel convolutionKernel = new Kernel(Integer.parseInt(kernelWidth), Integer.parseInt(kernelHeight));
        Kernel poolingKernel = new Kernel(2, 2);

        ConvolutionalNetwork convolutionNetwork = new ConvolutionalNetwork.ConvolutionalNetworkBuilder(inputDimension, 1)
                .withConvolutionLayer(convolutionKernel, Integer.parseInt(numberOfMaps[0]))
                .withPoolingLayer(poolingKernel)
                .withConvolutionLayer(convolutionKernel, Integer.parseInt(numberOfMaps[1]))
                .withPoolingLayer(poolingKernel)
                .withConvolutionLayer(convolutionKernel, Integer.parseInt(numberOfMaps[2]))
                .withFullConnectedLayer(Integer.parseInt(outputNeuronCount))
                .createNetwork();

        convolutionNetwork.setLabel(neuralNetworkName);
        ConvolutionalNetworkVisualPanel1.getInstance().clearForm();
        return convolutionNetwork; 
    }

    @Override
    public void storePanelData(Object settings) {
        ((WizardDescriptor) settings).putProperty("kernelWidth", ((ConvolutionalNetworkVisualPanel1) getComponent()).getKernelWidth().getText().trim());
        ((WizardDescriptor) settings).putProperty("kernelHeight", ((ConvolutionalNetworkVisualPanel1) getComponent()).getKernelHeigh().getText().trim());
        ((WizardDescriptor) settings).putProperty("inputWidth", ((ConvolutionalNetworkVisualPanel1) getComponent()).getInputWidth().getText().trim());
        ((WizardDescriptor) settings).putProperty("inputHeight", ((ConvolutionalNetworkVisualPanel1) getComponent()).getInputHeight().getText().trim());
        ((WizardDescriptor) settings).putProperty("numberOfMaps", ((ConvolutionalNetworkVisualPanel1) getComponent()).getNumberOfMaps().getText().trim());
        ((WizardDescriptor) settings).putProperty("outputNeuronCount", ((ConvolutionalNetworkVisualPanel1) getComponent()).getOutputNeurons().getText().trim());

    }

    @Override
    public void validatePanelData() throws WizardValidationException {
        int a=0;
        try {
            Integer.parseInt(((ConvolutionalNetworkVisualPanel1) getComponent()).getKernelWidth().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Kernel width must be integer value!", null);
        }
        try {
            Integer.parseInt(((ConvolutionalNetworkVisualPanel1) getComponent()).getKernelHeigh().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Kernel height must be integer value!", null);
        }

        try {
            Integer.parseInt(((ConvolutionalNetworkVisualPanel1) getComponent()).getInputWidth().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Input width must beinteger value!", null);
        }
        try {
            Integer.parseInt(((ConvolutionalNetworkVisualPanel1) getComponent()).getInputHeight().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Input height must be  integer value!", null);
        }
        try {
            Integer.parseInt(((ConvolutionalNetworkVisualPanel1) getComponent()).getOutputNeurons().getText().trim());
        } catch (NumberFormatException ex) {
            throw new WizardValidationException(null, "Output neuron count must be integer value!", null);
        }
        String[] numberOfMaps = ((ConvolutionalNetworkVisualPanel1) getComponent()).getNumberOfMaps().getText().trim().split(" ");

        for (String number : numberOfMaps) {
            try {
                Integer.parseInt(number);
            } catch (NumberFormatException ex) {
                throw new WizardValidationException(null, "Number of maps must be integer value seperated with space!", null);
            }
        }

    }

}
