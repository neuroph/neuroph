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

package org.neuroph.imgrec;

/**
 * This exception is thrown when image vector size is different than
 * input vector size which is accepted by neural network
 * @author Zoran Sevarac
 */
public class ImageSizeMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ImageSizeMismatchException() {
        super();
    }

    public ImageSizeMismatchException(String message) {
        super(message);
    }

    public ImageSizeMismatchException(String message, Throwable arg1) {
        super(message, arg1);
    }

    public ImageSizeMismatchException(Throwable arg0) {
        super(arg0);
    }
}

