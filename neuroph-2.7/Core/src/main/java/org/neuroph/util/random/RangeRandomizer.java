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
 * This class provides ranged weights randomizer, which randomize weights in specified [min, max] range.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RangeRandomizer extends WeightsRandomizer {
    /**
     * Lower range limit
     */
    protected double min;
    
    /**
     * Upper range limit
     */
    protected double max;

    /**
     * Creates a new instance of RangeRandomizer within specified .
     * The random values are generated according to formula:
     * newValue = min + random * (max - min)
     * @param min min weight value
     * @param max max weight value
     */
    public RangeRandomizer(double min, double max) {
        this.max = max;
        this.min = min;
    }

    /**
     * Generates next random value within [min, max] range determined by the settings in this randomizer
     * @return next weight random value
     */
    @Override
    protected double nextRandomWeight() {
        return min + randomGenerator.nextDouble() * (max - min);
    }
}
