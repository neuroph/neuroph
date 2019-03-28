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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Implementation of OutputAdapter interface for writing neural network outputs to output stream.
 * @see OutputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class OutputStreamAdapter implements OutputAdapter {

    protected BufferedWriter bufferedWriter;

    /**
     * Creates a new OutputStreamAdapter for specified output stream.
     */      
    public OutputStreamAdapter(OutputStream outputStream) {
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Creates a new OutputStreamAdapter for specified BufferedWriter.
     */         
    public OutputStreamAdapter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    /**
     * Writes specified output to output stream
     * @param output output vector to write
     */
    @Override
    public void writeOutput(double[] output) {
        try {
            StringBuilder outputLine = new StringBuilder();
            for (int i = 0; i < output.length; i++) {
                outputLine.append(output[i]).append(' ').append(outputLine);
            }
            outputLine.append(System.lineSeparator());
            
            bufferedWriter.write(outputLine.toString());
        } catch (IOException ex) {
            throw new NeurophOutputException("Error writing output to stream!", ex);
        }
    }

    /**
     * Closes output stream.
     */
    @Override
    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException ex) {
            throw new NeurophOutputException("Error closing output stream!", ex);
        }
    }
}
