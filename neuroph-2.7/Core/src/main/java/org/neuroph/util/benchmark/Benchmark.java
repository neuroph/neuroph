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

import java.util.ArrayList;

/**
 * This class is main benchmark driver. It holds collection of benchmarking tasks and provides benchmarking workflow.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Benchmark {
    ArrayList<BenchmarkTask> tasks;
    
    /**
     * Creates a new Benchmark instance
     */
    public Benchmark() {
        tasks = new ArrayList<BenchmarkTask>();
    }
    
    /**
     * Adds specified benchmark task
     * @param task benchmark task
     */
    public void addTask(BenchmarkTask task) {
        tasks.add(task);
    }
    
    /**
     * Runs specified benchmark tasks, the basic benchmarking workflow.
     * Prepares benchmark, run warming up iterations, measures the execution
     * time for specified number of benchmarking iterations, and gets the benchmarking results
     * @param task 
     */
    public static void runTask(BenchmarkTask task) {
        System.out.println("Preparing task " + task.getName());
        task.prepareTest();
        
        System.out.println("Warming up " + task.getName());
        for (int i = 0; i < task.getWarmupIterations(); i++) {
            task.runTest();
        }

        System.out.println("Runing " + task.getName());
        //task.prepare(); // ovde mozda poziv nekoj reset ili init metodi koja bi randomizovala mrezu, u osnovnoj klasi da neradi nista tako da moze da se redefinise i ne mora
        
        Stopwatch timer = new Stopwatch();
        BenchmarkTaskResults results = new BenchmarkTaskResults(task.getTestIterations());
        
        for (int i = 0; i < task.getTestIterations(); i++) {
            timer.reset();
            
            timer.start();            
            task.runTest();
            timer.stop();
            
            results.addElapsedTime(timer.getElapsedTime());
            System.gc();
        }   
        
        results.calculateStatistics(); 
        System.out.println(task.getName() + " results");
        System.out.println(results); // could be sent to file
    }
    
    /**
     * Runs all benchmark tasks
     */
    public void run() {
        for(int i=0; i < tasks.size(); i++) 
            runTask(tasks.get(i));        
    }    
    
}
