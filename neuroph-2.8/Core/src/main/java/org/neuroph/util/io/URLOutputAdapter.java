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
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implementation of OutputAdapter interface for writing neural network outputs to URL.
 * @see OutputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class URLOutputAdapter extends OutputStreamAdapter {
    
    /**
     * Creates a new URLOutputAdapter by opening a connection to URL specified by the url input param
     * @param url URL object to connect to.
     * @throws IOException if connection 
     */       
    public URLOutputAdapter(URL url) throws IOException {
        super(new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream())));     
    }  
    
    /**
     * Creates a new URLOutputAdapter by opening a connection to URL specified by the string url input param
     * @param url URL to connect to as string.
     * @throws IOException if connection 
     */        
    public URLOutputAdapter(String url) throws MalformedURLException, IOException {
        this(new URL(url));
    }
}