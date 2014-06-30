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
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implementation of InputAdapter interface for reading neural network inputs from URL.
 * @see InputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class URLInputAdapter extends InputStreamAdapter {

    /**
     * Creates a new URLInputAdapter by opening a connection to URL specified by the input param
     * @param url URL object to connect to.
     * @throws IOException if connection 
     */    
    public URLInputAdapter(URL url) throws IOException {
        super(new BufferedReader( new InputStreamReader( url.openStream() )));
    }
    
    /**
     * Creates a new URLInputAdapter by opening a connection to URL specified by the input param
     * @param url URL to connect to as string.
     * @throws IOException if connection 
     */       
    public URLInputAdapter(String url) throws MalformedURLException, IOException  {
        this(new URL(url));
    }    
      
}
