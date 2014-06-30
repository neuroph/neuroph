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

package org.neuroph.util;

import java.util.HashMap;

/**
 * Represents a general set of properties for neuroph objects
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Properties extends HashMap {
	private static final long serialVersionUID = 1L;


        protected void createKeys(String ... keys) {
            for(int i=0; i<keys.length; i++)
		this.put(keys[i], "");
	}

	public void setProperty(String key, Object value) {
//                if (!this.containsKey(key))
//                    throw new RuntimeException("Unknown property key: "+key);

		this.put(key, value);
	}

        public Object getProperty(String key) {
//                if (!this.containsKey(key))
//                        throw new RuntimeException("Unknown property key: "+key);

                return this.get(key);
        }

        public boolean hasProperty(String key) {
            return this.containsKey(key);
        }

}
