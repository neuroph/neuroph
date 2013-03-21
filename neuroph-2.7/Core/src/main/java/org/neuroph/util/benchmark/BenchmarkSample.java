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
 * This is an example how to use Neuroph microbenchmarking framework
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BenchmarkSample {

    public static void main(String[] args) {
        // create ans instance of benchmaring task
        BenchmarkTask task1 = new MyBenchmarkTask("MyFirstBenchmark");
        // set number of warmup iterations
        task1.setWarmupIterations(1);
        // set number of benchmarking iterations
        task1.setTestIterations(1);
        
        // create benchmark instance
        Benchmark benchmark = new Benchmark();
        // add task to benchmark
        benchmark.addTask(task1);
        // run benchmark
        benchmark.run();    
    }    
}