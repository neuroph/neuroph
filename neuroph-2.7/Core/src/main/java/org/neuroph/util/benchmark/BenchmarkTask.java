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
 * This class is an abstract base class for specific microbenchmarking tasks
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class BenchmarkTask {
        
    private String name;
    private int warmupIterations=1;
    private int testIterations=1;
                
    /**
     * Creates a new instance of BenchmarkTask with specified name
     * @param name benchmark task name
     */
    public BenchmarkTask(String name) {
        this.name = name;
    }

    /**
     * Gets task name
     * @return task name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets task name
     * @param name task name
     */
    public void setName(String name) {
        this.name = name;
    }    

    /**
     * Gets number of test (benchmarking) iterations
     * @return number of test iterations
     */
    public int getTestIterations() {
        return testIterations;
    }

    /**
     * Sets number of test (benchmarking) iterations
     * @param testIterations number of test iterations
     */
    public void setTestIterations(int testIterations) {
        this.testIterations = testIterations;
    }

    /**
     * Gets number of warmup iterations.
     * Warmup iterations are used to run test for some time to stabilize JVM (compiling, optimizations, gc)
     * @return number of warmup iterations 
     */
    public int getWarmupIterations() {
        return warmupIterations;
    }

    /**
     * Sets the number of warmup iterations 
     * @param warmupIterations number of warmup iterations 
     * Warmup iterations are used to run test for some time to stabilize JVM (compiling, optimizations, gc)
     */
    public void setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
    }
        

    /**
     * Any initialization before running performance test (benchmark) goes here
     */
    abstract public void prepareTest();
    
    /**
     * This method should hold the code to benchmark
     */
    abstract public void runTest();        
}