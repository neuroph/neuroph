package org.neuroph.netbeans.wizards;

/**
 *
 * @author Marko
 */
public class NeuralNetworkTypeWizard {

    public static final int EMPTY_NN = 0;
    public static final int ADALINE=1;
    public static final int PERCEPTRON=2;
    public static final int MULTI_LAYER_PERCEPTRON=3;
    public static final int HOPFIELD=4;
    public static final int BAM=5;
    public static final int KOHONEN=6;
    public static final int SUPERVISED_HEBBIAN=7;
    public static final int UNSUPERVISED_HEBBIAN=8;
    public static final int MAXNET=9;
    public static final int COMPETITIVE_NETWORK=10;
    public static final int RBF=11;
    public static final int INSTAR=12;
    public static final int OUTSTAR=13;
    public static final int NOPROP=14;
    
    

    private static NeuralNetworkTypeWizard instance;

    private int neuralNetworkType;

    public static NeuralNetworkTypeWizard getInstance(){
        if(instance==null){
            instance = new NeuralNetworkTypeWizard();
        }
        return instance;
    }

    public void setType(int type){
        switch(type){
            case ADALINE: neuralNetworkType= ADALINE; break;
            case PERCEPTRON: neuralNetworkType= PERCEPTRON; break;
            case MULTI_LAYER_PERCEPTRON: neuralNetworkType= MULTI_LAYER_PERCEPTRON; break;
            case HOPFIELD: neuralNetworkType= HOPFIELD; break;
            case BAM: neuralNetworkType= BAM; break;
            case KOHONEN: neuralNetworkType= KOHONEN; break;
            case SUPERVISED_HEBBIAN: neuralNetworkType= SUPERVISED_HEBBIAN; break;
            case UNSUPERVISED_HEBBIAN: neuralNetworkType= UNSUPERVISED_HEBBIAN;break;
            case MAXNET: neuralNetworkType= MAXNET; break;
            case COMPETITIVE_NETWORK: neuralNetworkType= COMPETITIVE_NETWORK; break;
            case RBF: neuralNetworkType= RBF; break;
            case INSTAR: neuralNetworkType= INSTAR; break;
            case OUTSTAR: neuralNetworkType= OUTSTAR; break;
            case EMPTY_NN : neuralNetworkType = EMPTY_NN; break;    
            case NOPROP : neuralNetworkType = NOPROP; break;    
        }
    }

    public int getType(){
        return neuralNetworkType;
    }

}
