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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Implementation of InputAdapter interface for reading neural network inputs from files.
 * @see InputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class FileInputAdapter extends InputStreamAdapter {

    /**
     * Creates a new FileInputAdapter by opening a connection to an actual file,
     * specified by the file param
     * @param file File object in the file system
     * @throws FileNotFoundException if specified file was not found
     */
    public FileInputAdapter(File file) throws FileNotFoundException {
        super(new BufferedReader(new FileReader(file)));
    }
    
    /**
     * Creates a new FileInputAdapter by opening a connection to an actual file,
     * specified by the fileName param
     * @param fileName name of the file in file system
     * @throws FileNotFoundException if specified file was not found
     */    
    public FileInputAdapter(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }    
    
}