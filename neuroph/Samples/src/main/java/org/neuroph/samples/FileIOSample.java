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

package org.neuroph.samples;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.io.FileInputAdapter;
import org.neuroph.util.io.FileOutputAdapter;
import org.neuroph.util.io.IOHelper;
import org.neuroph.util.io.InputAdapter;
import org.neuroph.util.io.OutputAdapter;

/**
 * This sample shows how to read from and write to external data sources using 
 * Neuroph file IO adapters.
 * 
 * Note: Just make sure you specify right file names/path below, when you run 
 * this example, otherwise you'll get FileNotFoundException.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see InputAdapter
 * @see OutputAdapter
 * @see IOHelper
 */
public class FileIOSample {
    
    /**
     * Runs this sample
     */    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // create neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(2, 3, 1); 
        
        // use file provided in org.neuroph.sample.data package
        String inputFileName = FileIOSample.class.getResource("data/xor_data.txt").getFile();
        // create file input adapter using specifed file
        FileInputAdapter fileIn = new FileInputAdapter(inputFileName);
        // create file output  adapter using specified file name
        FileOutputAdapter fileOut = new FileOutputAdapter("some_output_file.txt");
              
        
        double[] input; // input buffer used for reading network input from file
        // read network input using input adapter
        while( (input = fileIn.readInput()) != null) {
            // feed neywork with input    
            neuralNet.setInput(input);
            // calculate network ...
            neuralNet.calculate();  
            // .. and get network output
            double[] output = neuralNet.getOutput();
            // write network output using output adapter
            fileOut.writeOutput(output);
        }
        
        // close input and output files
        fileIn.close();
        fileOut.close();     
        
        // Also note that shorter way for this is using org.neuroph.util.io.IOHelper class
    }
}
