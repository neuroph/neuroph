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
 * This class holds benchmarking results, elapsed times for all iterations and 
 * various statistics min, max, avg times and standard deviation
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BenchmarkTaskResults {

    int testIterations;
    long[] elapsedTimes;
    int timesCounter;
    double averageTestTime;
    double standardDeviation;
    double minTestTime;
    double maxTestTime;

    public BenchmarkTaskResults(int testIterations) {
        this.testIterations = testIterations;
        this.elapsedTimes = new long[testIterations];
        this.timesCounter = 0;
    }

    public double getAverageTestTime() {
        return averageTestTime;
    }

    public long[] getElapsedTimes() {
        return elapsedTimes;
    }

    public double getMaxTestTime() {
        return maxTestTime;
    }

    public double getMinTestTime() {
        return minTestTime;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public int getTestIterations() {
        return testIterations;
    }

    public void addElapsedTime(long time) {
        this.elapsedTimes[timesCounter++] = time;
    }

    public void calculateStatistics() {

        this.minTestTime = elapsedTimes[0];
        this.maxTestTime = elapsedTimes[0];
        long sum = 0;

        for (int i = 0; i < timesCounter; i++) {
            sum += elapsedTimes[i];
            if (elapsedTimes[i] < minTestTime) {
                minTestTime = elapsedTimes[i];
            }
            if (elapsedTimes[i] > maxTestTime) {
                maxTestTime = elapsedTimes[i];
            }
        }

        this.averageTestTime = sum / (double) timesCounter;
        
        //  std. deviation
        long sqrSum = 0;
        
        for(int i = 0; i< timesCounter; i++) {
            sqrSum += (elapsedTimes[i] - averageTestTime) * (elapsedTimes[i] - averageTestTime);
        }

        this.standardDeviation = Math.sqrt(sqrSum / (double)timesCounter);
    }
    
    public String toString() {
        String results = "Test iterations: " + testIterations  + "\n" +
               "Min time: " + minTestTime  + " ms\n" +
               "Max time: " + maxTestTime  + " ms\n" +
               "Average time: " + averageTestTime  + " ms\n" +
               "Std. deviation: " + standardDeviation  + "\n";
        
        results += "Test times:\n";
        
        for(int i=0; i<timesCounter; i++)
            results += (i+1) +". iteration: " + elapsedTimes[i] + "ms\n";
        
        return results;
    }
}
