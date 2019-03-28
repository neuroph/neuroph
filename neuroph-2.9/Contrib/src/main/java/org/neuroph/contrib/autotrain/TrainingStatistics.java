/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.autotrain;

import java.util.List;

/**
 *
 * @author Milan Brkic - milan.brkic1@yahoo.com
 */
public class TrainingStatistics {
    double min;
    double max;
    double mean;
    double std;
    
    /**
     *
     * @param min
     * @param max
     * @param mean
     * @param std
     */
    public TrainingStatistics(double min, double max, double mean, double std) {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.std = std;
    }

    /**
     *
     */
    public TrainingStatistics(){
        
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }
    
    
    
    /**
     *Based on list of TrainingResults this method calculate minimum iterations, maximum iterations, mean and standard deviation of list.
     * @param list
     * @return
     */
    public static TrainingStatistics calculateIterations(List<TrainingResult> list) {
        int minIt = list.get(0).getIterations();
        int maxIt = list.get(0).getIterations();
        double meanIt;
        double stdIt;
        
        int sumIt = 0;
        double[] v = new double[list.size()];
        int i = 0;
        for(TrainingResult tr:list){
            if(tr.getIterations() < minIt){
                minIt = tr.getIterations();
            }
            if(tr.getIterations() > maxIt){
                maxIt = tr.getIterations();
            }
            sumIt += tr.getIterations();
            v[i++] = tr.getIterations();
        }
        
        meanIt = sumIt / list.size();
        
        stdIt = std(meanIt,v);
        
        return new TrainingStatistics(minIt, maxIt, meanIt, stdIt);
    }
    
    /**
     *Based on list of TrainingResults this method calculate minimum MSE, maximum MSE, mean and standard deviation of list.
     * @param list
     * @return
     */
    public static TrainingStatistics calculateMSE(List<TrainingResult> list) {
        double minIt = list.get(0).getTotalError();
        double maxIt = list.get(0).getTotalError();
        double meanIt;
        double stdIt;
        
        double pom = 0;
        double[] vrednosti = new double[list.size()];
        int i = 0 ;
        for(TrainingResult tr:list){
            if(tr.getTotalError() < minIt){
                minIt = tr.getTotalError();
            }
            if(tr.getTotalError() > maxIt){
                maxIt = tr.getTotalError();
            }
            pom += tr.getTotalError();
            
            vrednosti[i++] = tr.getTotalError();
        }
        
        meanIt = pom / list.size();
        
        stdIt = std(meanIt, vrednosti);
        
        return new TrainingStatistics(minIt, maxIt, meanIt, stdIt);
    }

    public static TrainingStatistics calculateStatistic(double[] array) {
        double min = array[0];
        double max = array[0];
        double mean = 0;
        double std = 0;
        
        double sum = 0;

        for(int i = 0; i < array.length; i++){
            if(array[i] < min){
                min = array[i];
            }
            if(array[i] > max){
                max = array[i];
            }
            sum += array[i];
            
        }
        mean = sum / array.length;
        
        std = std(mean, array);
        
        return new TrainingStatistics(min, max, mean, std);
    }
    
    private static TrainingStatistics calculateStatistic(int[] array) {
        int min = array[0];
        int max = array[0];
        double mean = 0;
        double std = 0;
        
        int sum = 0;

        for(int i = 0; i < array.length; i++){
            if(array[i] < min){
                min = array[i];
            }
            if(array[i] > max){
                max = array[i];
            }
            sum += array[i];
            
        }
        mean = sum / array.length;
        
        std = std(mean, array);
        
        return new TrainingStatistics(min, max, mean, std);
    }
    @Override
    public String toString() {
        return "Statistic{" + "min=" + min + ", max=" + max + ", mean=" + mean + ", std=" + std + '}';
    }
    
    
    
    private static double std(double mean, int[] sum) {
        double std = 0 ;
        
        for(double temp : sum){
            std += Math.pow(temp - mean, 2); 
        }
        
        return Math.sqrt(std /sum.length);
    }

 
    private static double std(double mean, double[] sum) {
        double std = 0 ;
        
        for(double temp : sum){
            std += Math.pow(temp - mean, 2); 
        }
        
        return Math.sqrt(std /sum.length);
    }
}
