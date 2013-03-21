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

package org.neuroph.util.benchmark;

/**
 * A class to help benchmark code, it simulates a real stop watch.
 * @see <a href="http://java.sun.com/docs/books/performance/1st_edition/html/JPMeasurement.fm.html#17818">http://java.sun.com/docs/books/performance/1st_edition/html/JPMeasurement.fm.html#17818</a>
 */
public class Stopwatch {

    private long startTime = -1;
    private long stopTime = -1;
    private boolean running = false;
    

    public Stopwatch() {
    }
    

    /**
     * Starts measuring time
     */
    public void start() {
        startTime = System.currentTimeMillis();
        running = true;
    }

    /**
     * Stops measuring time
     */
    public void stop() {
        stopTime = System.currentTimeMillis();
        running = false;
    }

    /** 
     * Returns elapsed time in milliseconds between calls to start and stop methods
     * If the watch has never been started, returns zero
     */
    public long getElapsedTime() {
        if (startTime == -1) {
            return 0;
        }
        if (running) {
            return System.currentTimeMillis() - startTime;
        } else {
            return stopTime - startTime;
        }
    }
    
    /**
     * Resets the stopwatch (clears start and stop time)
     */
    public void reset() {
        startTime = -1;
        stopTime = -1;
        running = false;
    }
}