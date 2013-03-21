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

package org.neuroph.core.exceptions;

/**
 * Thrown to indicate that vector size does not match the network input or training element size.
 *
 * @author Jon Tain
 * @author Zoran Sevarac
 */
public class VectorSizeMismatchException extends NeurophException {

    private static final long serialVersionUID = 2L;

    /**
     * Constructs an VectorSizeMismatchException with no detail message.
     */
    public VectorSizeMismatchException() {
        super();
    }

    /**
     * Constructs an VectorSizeMismatchException with the specified detail message.
     * @param message the detail message.
     */
    public VectorSizeMismatchException(String message) {
        super(message);
    }

    /**
     * Constructs a VectorSizeMismatchException with the specified detail message and specified cause.
     * @param message the detail message.
     * @param cause the cause for exception
     */
    public VectorSizeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause
     * @param cause the cause for exception
     */
    public VectorSizeMismatchException(Throwable cause) {
        super(cause);
    }
}
