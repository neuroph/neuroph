/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
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

package org.neuroph.util.random;

/**
 * This class provides Gaussian randomization technique using Box Muller method.
 * Based on GaussianRandomizer from Encog
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class GaussianRandomizer extends WeightsRandomizer {

    double mean;
    double standardDeviation;
 
    /**
     * The y2 value.
     */
    private double y2;
    
    /**
     * Should we use the last value.
     */
    private boolean useLast = false;

    public GaussianRandomizer(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    /**
     * Compute a Gaussian random number.
     * 
     * @param mean
     *            The mean.
     * @param std
     *            The standard deviation.
     * @return The random number.
     */
     private double boxMuller(double mean, double std) {
        double x1, x2, w, y1;

        // use value from previous call
        if (this.useLast) {
            y1 = this.y2;
            this.useLast = false;
        } else {
            do {
                x1 = 2.0 * randomGenerator.nextDouble() - 1.0;
                x2 = 2.0 * randomGenerator.nextDouble() - 1.0;
                w = x1 * x1 + x2 * x2;
            } while (w >= 1.0);

            w = Math.sqrt((-2.0 * Math.log(w)) / w);
            y1 = x1 * w;
            this.y2 = x2 * w;
            this.useLast = true;
        }

        return (mean + y1 * std);
    }

    @Override
    protected double nextRandomWeight() {
        return boxMuller(mean, standardDeviation);
    }
}
