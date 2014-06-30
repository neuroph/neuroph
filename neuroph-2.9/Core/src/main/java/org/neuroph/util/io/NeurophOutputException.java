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

import org.neuroph.core.exceptions.NeurophException;

/**
 * This exception is thrown when some error occurs when writing neural network
 * output using some output adapter.
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see OutputAdapter
 */
public class NeurophOutputException extends NeurophException {
   
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an NeurophOutputException with no detail message.
     */
    public NeurophOutputException() {
        super();
    }

    /**
     * Constructs an NeurophOutputException with the specified detail message.
     * @param message the detail message.
     */
    public NeurophOutputException(String message) {
        super(message);
    }

    /**
     * Constructs a NeurophOutputException with the specified detail message and specified cause.
     * @param message the detail message.
     * @param cause the cause for exception
     */
    public NeurophOutputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause
     * @param cause the cause for exception
     */
    public NeurophOutputException(Throwable cause) {
        super(cause);
    }       
    
}
