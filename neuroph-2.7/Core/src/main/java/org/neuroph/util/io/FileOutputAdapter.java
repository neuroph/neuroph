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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation of OutputAdapter interface for writing neural network outputs to files.
 * @see OutputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class FileOutputAdapter extends OutputStreamAdapter {

    /**
     * Creates a new FileOutputAdapter by opening a connection to an actual file,
     * specified by the file param
     * @param file File object in the file system
     * @throws FileNotFoundException if specified file was not found
     * @throws IOException
     */    
    public FileOutputAdapter(File file) throws FileNotFoundException, IOException {
        super(new BufferedWriter(new FileWriter(file)));
    }
    
    /**
     * Creates a new FileOutputAdapter by opening a connection to an actual file,
     * specified by the fileName param
     * @param fileName name of the file in file system
     * @throws FileNotFoundException if specified file was not found
     * @throws IOException
     */       
    public FileOutputAdapter(String fileName) throws FileNotFoundException, IOException {
        this(new File(fileName));
    }    
     
}
