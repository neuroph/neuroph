/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.autotrain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author Milan Brkic - milan.brkic1@yahoo.com
 */
public class Util {

    private static final String FILENAME = "result.csv";

    /**
     * Generate header of a file and add data.
     *
     * @param trainer
     * @param results
     * @throws FileNotFoundException
     */
    public static void saveToCSV(AutoTrainer trainer, List<TrainingResult> results) throws FileNotFoundException {
        System.out.println("Creating 'result.csv'...");
        StringBuilder sb = new StringBuilder();
        boolean split = trainer.isSplitForTesting();
        boolean stat = trainer.generatesStatistics();

        //creating header of document
        sb.append("Learning rate, Hidden Neurons, Momentum, Max Error, ");
        boolean pom = true;
        if (split) {
            sb.append("Training Set(%), Test Set(%), ");
        }

        if (stat) {
            sb.append("Min Iterations, Max Iterations,Mean Iterations,STD Iterations, Min MSE,Max MSE,Mean MSE,STD MSE,");
            pom = false;
        }
        if (pom) {
            sb.append("Iterations, Total error, ");
        }
        sb.append("\n");
        //done creating header of document

        //loops through TrainingResult list and writing data in StringBuilder
        for (TrainingResult tr : results) {
            double lr = tr.getSettings().getLearningRate();
            int hidden = tr.getSettings().getHiddenNeurons();
            double momentum = tr.getSettings().getMomentum();
            double error = tr.getSettings().getMaxError();
            sb.append(lr);
            sb.append(',');
            sb.append(hidden);
            sb.append(',');
            sb.append(momentum);
            sb.append(',');
            sb.append(error);
            sb.append(',');
            if (split) {
                sb.append(tr.getSettings().getTrainingSet());
                sb.append(',');
                sb.append(tr.getSettings().getTestSet());
                sb.append(',');
            }
            if (stat) {
                sb.append(tr.getIterationStat().getMin());
                sb.append(',');
                sb.append(tr.getIterationStat().getMax());
                sb.append(',');
                sb.append(tr.getIterationStat().getMean());
                sb.append(',');
                sb.append(tr.getIterationStat().getStd());
                sb.append(',');
                sb.append(new BigDecimal(tr.getMSE().getMin()).setScale(8, RoundingMode.HALF_UP));
                sb.append(',');
                sb.append(new BigDecimal(tr.getMSE().getMax()).setScale(8, RoundingMode.HALF_UP));
                sb.append(',');
                sb.append(new BigDecimal(tr.getMSE().getMean()).setScale(8, RoundingMode.HALF_UP));
                sb.append(',');
                sb.append(new BigDecimal(tr.getMSE().getStd()).setScale(8, RoundingMode.HALF_UP));
                sb.append(',');
            }
            if (pom) {
                sb.append(tr.getIterations());
                sb.append(',');
                sb.append(tr.getTotalError());
                sb.append(',');
            }
            sb.append('\n');
        }
        //done writing data in StringBuilder
        try (PrintWriter pw = new PrintWriter(new File(FILENAME))) {
            pw.write(sb.toString());
        }
        System.out.println("'result.csv' successfully created.");
    }

}
