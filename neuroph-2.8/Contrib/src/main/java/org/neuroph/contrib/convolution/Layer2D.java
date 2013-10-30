package org.neuroph.contrib.convolution;

import java.io.Serializable;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 * 2D Layer used as feature map for convolutional networks
 *
 * @author Boris Fulurija
 */
public class Layer2D extends Layer {

    private static final long serialVersionUID = 8498669699995172395L;
    /**
     * 2d layer dimensions (width and height)
     */
    private Dimension dimension;

    /**
     *
     * @param dimension
     */
    public Layer2D(Dimension dimension) {
        this.dimension = dimension;
    }
    
    public Layer2D(Dimension dimension, NeuronProperties neuronProperties) {
        this(dimension);
        
      for (int i = 0; i < dimension.getHeight() * dimension.getWidth(); i++) {
            Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
            addNeuron(neuron);
        }        
    }    

    /**
     * Returns width of this layer
     *
     * @return width of this layer
     */
    public int getWidth() {
        return dimension.getWidth();
    }

    /**
     * Returns height of this layer
     *
     * @return height of this layer
     */
    public int getHeight() {
        return dimension.getHeight();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
    
    
    
    

    /**
     * Returns neuron at specified position in this layer
     *
     * @param x
     * @param y
     * @return
     */
    public Neuron getNeuronAt(int x, int y) {
        return getNeuronAt(x + y * (dimension.getWidth()));
    }

    /**
     * This class representas dimensions (width and height) of the Layer2D
     *
     */
    public static class Dimension implements Serializable {

        private static final long serialVersionUID = -4491706467345191107L;
        private int width;
        private int height;

        public Dimension(int width, int height) {
            super();
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            String dimension = "Width = " + width + "; Height = " + height;
            return dimension;
        }
    }
}
