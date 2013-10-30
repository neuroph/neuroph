package org.neuroph.contrib.convolution;

import org.neuroph.core.Neuron;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class ConvolutionLayer extends FeatureMapsLayer {

    private static final long serialVersionUID = -4619196904153707871L;
    
    public static NeuronProperties neuronProperties = new NeuronProperties();
    
    static {
        neuronProperties.setProperty("useBias", true);
        neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
        neuronProperties.setProperty("inputFunction", WeightedSum.class);    
    }

    public ConvolutionLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
        super(kernel);               
        Layer2D.Dimension fromDimension = fromLayer.getDimension();
        
        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.mapDimension = new Layer2D.Dimension(mapWidth, mapHeight);
    }
    
    
    
    
    
  //  public ConvolutionLayer(Kernel kernel, Layer2D.Dimension mapDimension) {
 //       super(kernel, mapDimension);     
   // also specify how many maps so they can be created here
        
//        for (int i = 0; i < mapDimension.getHeight() * mapDimension.getWidth(); i++) {
//            Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
//            addNeuron(neuron);
//        }
  //  }
    
//    public ConvolutionLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
//        Layer2D.Dimension fromDimension = fromLayer.getDimension();     
//        Layer2D.Dimension toDimension;        
//        
//	int toMapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
//			int toMapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
//			toDimension = new Layer2D.Dimension(toMapWidth, toMapHeight);
//        this(kernel, toDimension);
//                        
//    }
    
    
}
