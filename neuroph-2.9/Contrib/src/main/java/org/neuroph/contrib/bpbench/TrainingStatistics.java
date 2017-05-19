/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that defines statistics for training`s
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class TrainingStatistics {

    /**
     * Minimum iterations for specific training repeated n times
     */
    private int minIterations = Integer.MAX_VALUE;
    /**
     * Maximum iterations for specific training repeated n times
     */
    private int maxIterations = Integer.MIN_VALUE;
    /**
     * Mean of iterations for specific training repeated n times
     */
    private double meanIterations;
    /**
     * Standard deviation of iterations for specific training repeated n times
     */
    private double stdIterations;
    /**
     * Minimum total error for specific training repeated n times
     */
    private double minError = Integer.MAX_VALUE;
    /**
     * Maximum total error for specific training repeated n times
     */
    private double maxError = Integer.MIN_VALUE;
    /**
     * Mean of total error for specific training repeated n times
     */
    private double meanError;
    /**
     * Standard deviation of total error for specific training repeated n times
     */
    private double stdError;
    /**
     * List of training results for specific training repeated n times
     */
    private List<TrainingResult> trainingResults = new ArrayList<>();

    /**
     * Method that adds new training result to statistic and set minimum and
     * maximum iterations and total error
     *
     * @param result
     */
    public void addData(TrainingResult result) {
        if (result.getTrainingIterations() < minIterations) {
            minIterations = result.getTrainingIterations();
        }
        if (result.getTrainingIterations() > maxIterations) {
            maxIterations = result.getTrainingIterations();
        }
        if (result.getError() < minError) {
            minError = result.getError();
        }
        if (result.getError() > maxError) {
            maxError = result.getError();
        }
        trainingResults.add(result);

    }

    /**
     * Method for calculating mean and standard deviation
     */
    public void calculateParameters() {
        calculateMean();
        calculateStd();
    }

    /**
     * Method for calculating mean of iterations and total error
     */
    private void calculateMean() {
        int sumIter = 0;
        double sumError = 0;
        for (int i = 0; i < trainingResults.size(); i++) {
            sumError += trainingResults.get(i).getError();
            sumIter += trainingResults.get(i).getTrainingIterations();
        }
        meanError = sumError / trainingResults.size();
        meanIterations = (double) sumIter / (double) trainingResults.size();
    }

    /**
     * Method for calculating standard deviation of iterations and total error
     */
    private void calculateStd() {
        if (meanError == 0 || meanIterations == 0) {
            calculateMean();
        }
        double sumError = 0;
        int sumIter = 0;
        for (TrainingResult trainingResult : trainingResults) {
            sumError += Math.pow(trainingResult.getError() - meanError, 2);
            sumIter += Math.pow(trainingResult.getTrainingIterations() - meanIterations, 2);
        }
        stdError = Math.sqrt(sumError / trainingResults.size()); // da li sa size ili sa size-1
        stdIterations = Math.sqrt(sumIter / trainingResults.size());
    }

    /**
     * Create instance of training statistic
     */
    public TrainingStatistics() {

    }

    /**
     * Return minimum iterations
     *
     * @return
     */
    public int getMinIterations() {
        return minIterations;
    }

    /**
     * Return maximum iterations
     *
     * @return
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    /**
     * Return list of all training results
     *
     * @return
     */
    public List<TrainingResult> getTrainingResults() {
        return trainingResults;
    }

    /**
     * Return mean of iterations for this training
     *
     * @return mean iterations
     */
    public double getMeanIterations() {
        return meanIterations;
    }

    /**
     * Return standard deviation of iterations for this training
     *
     * @return
     */
    public double getStdIterations() {
        return stdIterations;
    }

    @Override
    public String toString() {
        return "TrainingStatistics{" + "minIterations=" + minIterations + ", maxIterations=" + maxIterations + ", meanIterations=" + meanIterations + ", stdIterations=" + stdIterations + ", minError=" + minError + ", maxError=" + maxError + ", meanError=" + meanError + ", stdError=" + stdError + '}';
    }

    /**
     * Return minimum error for this training
     *
     * @return minimum error
     */
    public double getMinError() {
        return minError;
    }

    /**
     * Return maximum error for this training
     *
     * @return maximum error
     */
    public double getMaxError() {
        return maxError;
    }

    /**
     * Return mean error for this training
     *
     * @return mean error
     */
    public double getMeanError() {
        return meanError;
    }

    /**
     * Return standard deviation for this training
     *
     * @return standard deviation
     */
    public double getStdError() {
        return stdError;
    }

}
