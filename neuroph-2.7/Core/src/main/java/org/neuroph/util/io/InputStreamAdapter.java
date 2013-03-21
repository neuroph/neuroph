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

package org.neuroph.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.neuroph.util.VectorParser;

/**
 * Implementation of InputAdapter interface for reading neural network inputs from input stream.
 * @see InputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class InputStreamAdapter implements InputAdapter {
   
    protected BufferedReader bufferedReader;

    public InputStreamAdapter(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }
    
    public  InputStreamAdapter(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
        
    @Override
    public double[] readInput() {    
        try {
            String inputLine = bufferedReader.readLine();        
            if (inputLine != null) {
               double[] inputBuffer = VectorParser.parseDoubleArray(inputLine);        
               return inputBuffer;
            }
            return null;
        } catch (IOException ex) {
             throw new NeurophInputException("Error reading input from stream!", ex);
        }
    }
    
    @Override
    public void close() {
        try {
            if (bufferedReader != null)
                bufferedReader.close();
        } catch (IOException ex) {
            throw new NeurophInputException("Error closing stream!", ex);
        }
    }    
    
}