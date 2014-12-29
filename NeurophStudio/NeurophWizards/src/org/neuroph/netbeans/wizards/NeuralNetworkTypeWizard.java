package org.neuroph.netbeans.wizards;

public enum NeuralNetworkTypeWizard {

    EMPTY_NN(0), ADALINE(1), PERCEPTRON(2), MULTI_LAYER_PERCEPTRON(3), HOPFIELD(4), BAM(5), KOHONEN(6),
    SUPERVISED_HEBBIAN(7), UNSUPERVISED_HEBBIAN(8), MAXNET(9), COMPETITIVE_NETWORK(10), RBF(11), INSTAR(12), OUTSTAR(13), NOPROP(14), CONVOLUTIONAL_NETWORK(15);

    private final int neuralNetworkType;

    NeuralNetworkTypeWizard(int neuralNetworkType) {
        this.neuralNetworkType = neuralNetworkType;
    }

    public int getType() {
        return neuralNetworkType;
    }
    
    public static NeuralNetworkTypeWizard forInt(int id) {
        for (NeuralNetworkTypeWizard type : values()) {
            if (type.neuralNetworkType == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + id);
    }

}
