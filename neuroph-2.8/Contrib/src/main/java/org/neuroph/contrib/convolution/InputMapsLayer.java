package org.neuroph.contrib.convolution;

import static org.neuroph.contrib.convolution.PoolingLayer.DEFAULT_NEURON_PROP;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.Max;
import org.neuroph.core.transfer.Linear;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 * Input layer for convolutional networks
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class InputMapsLayer extends FeatureMapsLayer {

    private static final long serialVersionUID = -4982081431101626706L;
    
    public static final  NeuronProperties DEFAULT_NEURON_PROP = new NeuronProperties();

    static {
		DEFAULT_NEURON_PROP.setProperty("neuronType", InputNeuron.class);
		DEFAULT_NEURON_PROP.setProperty("transferFunction", Linear.class);     
    }
    
    
    public InputMapsLayer( Layer2D.Dimension dimension) {
        super(null, dimension);

//        // create feature map for input neurons
//        Layer2D featureMap = new Layer2D(dimension);
//        
//        // create input neurons and add them to feature map
//      //  NeuronProperties neuronProperties = new NeuronProperties(InputNeuron.class, Linear.class);        
//        for (int i = 0; i < dimension.getHeight() * dimension.getWidth(); i++) {
//            Neuron neuron = NeuronFactory.createNeuron(InputMapsLayer.DEFAULT_NEURON_PROP);
//            featureMap.addNeuron(neuron);
//        }
//        
//        addFeatureMap(featureMap);
    }

	@Override
	public void connectMaps(Layer2D fromMap, Layer2D toMap) {
		throw new UnsupportedOperationException("Input layer can't have previous layer!");
	}

}
