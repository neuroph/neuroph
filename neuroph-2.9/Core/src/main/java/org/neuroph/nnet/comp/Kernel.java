/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neuroph.nnet.comp;

import java.io.Serializable;
import org.neuroph.core.Weight;

/**
 * Kernel used in convolution networks. Kernel is (width x height) window
 * sliding over 2D input space. Each window position provides (width x height)
 * inputs for the neurons in next layer.
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class Kernel implements Serializable {

    private static final long serialVersionUID = -3948374914759253222L;

    /**
     * Kernel width
     */
    private int width;

    /**
     * Kernel height
     */
    private int height;

    private Weight[][] weights;

    
    /**
     * Creates new kernel with specified width and height
     *
     * @param width kernel width
     * @param height kernel height
     */
    public Kernel(Dimension2D dimension) {
        this.width = dimension.getWidth();
        this.height = dimension.getHeight();
    }    
    
    /**
     * Creates new kernel with specified width and height
     *
     * @param width kernel width
     * @param height kernel height
     */
    public Kernel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns width of this kernel
     *
     * @return width of this kernel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width of this kernel
     *
     * @param width kernel width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns height of this kernel
     *
     * @return height of this kernel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height of this kernel
     *
     * @param height kernel height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns area of this kernel (width*height)
     *
     * @return area of this kernel
     */
    public int getArea() {
        return width * height;
    }

    public Weight[][] getWeights() {
        return weights;
    }

    public void setWeights(Weight[][] weights) {
        this.weights = weights;
    }
    
    public void initWeights(double min, double max) {
        weights = new Weight[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Weight weight = new Weight();
                weight.randomize(min, max);
                weights[i][j] = weight;
            }
        }        
    }

}
