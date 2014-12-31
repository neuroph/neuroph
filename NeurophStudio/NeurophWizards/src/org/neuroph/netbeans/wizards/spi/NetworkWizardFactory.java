package org.neuroph.netbeans.wizards.spi;

import org.neuroph.netbeans.wizards.NeuralNetworkTypeWizard;

public class NetworkWizardFactory {

    public static NetworkWizard createNetworkWizard(NeuralNetworkTypeWizard type) {

        switch (type) {
            case ADALINE:
                return new AdalineNetworkWizard();
            case PERCEPTRON:
                return new PerceptronNetworkWizard();
            case MULTI_LAYER_PERCEPTRON:
                return new MultilayerPerceptronNetworkWizard();
            case HOPFIELD:
                return new HopfildNetworkWizard();
            case BAM:
                return new BAMNetworkWizard();
            case KOHONEN:
                return new KohenNetworkWizard();
            case SUPERVISED_HEBBIAN:
                return new SupervisedHebianNetworkWizard();
            case UNSUPERVISED_HEBBIAN:
                return new UnsupervisedHebianNetworkWizard();
            case MAXNET:
                return new MaxNetNetworkWizard();
            case COMPETITIVE_NETWORK:
                return new CompetitiveNetworkWizard();
            case RBF:
                return new RBFNetworkWizard();
            case INSTAR:
                return new InstarNetworkWizard();
            case OUTSTAR:
                return new OutstarNetworkWizard();
            case NOPROP:
                return new NoPropNetworkWizard();
            case CONVOLUTIONAL_NETWORK:
                return new ConvolutionalNetworkWizard();                    
            default:
                return null;
        }
    }
}
