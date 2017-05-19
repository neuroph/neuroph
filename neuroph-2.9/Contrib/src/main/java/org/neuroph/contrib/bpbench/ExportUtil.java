/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class ExportUtil {
/**
 * Delimiter used for generating csv
 */
    private static final String DELIMITER = ", ";
/**
 * Method for writing to csv into given file path
 * @param filePath
 * @param listOfTrainings
 * @throws IOException 
 */
    public static void exportToCSV(String filePath, List<AbstractTraining> listOfTrainings) throws IOException {
        FileWriter out = new FileWriter(filePath + ".csv");

        out.write(writeheader());
        for (AbstractTraining training : listOfTrainings) {
            out.write(prepareLine(training));
        }
        out.flush();
        out.close();
    }
/**
 * Method for generating one line from training
 * @param training
 * @return 
 */
    private static String prepareLine(AbstractTraining training) {
        String line = "";
        line += training.getNeuralNet().getLearningRule().getClass().getName() + DELIMITER;
        line += training.getStats().getMinIterations() + DELIMITER;
        line += training.getStats().getMaxIterations() + DELIMITER;
        line += training.getStats().getMeanIterations() + DELIMITER;
        line += training.getStats().getStdIterations() + DELIMITER;
        line += training.getStats().getMinError() + DELIMITER;
        line += training.getStats().getMaxError() + DELIMITER;
        line += training.getStats().getMeanError() + DELIMITER;
        line += training.getStats().getStdError() + DELIMITER;

        line += training.getSettings().getLearningRate() + DELIMITER;
        line += training.getSettings().getMomentum() + DELIMITER;
        line += training.getSettings().getMaxIterations() + DELIMITER;
        line += training.getSettings().getMaxError() + DELIMITER;
        line += String.valueOf(training.getSettings().isBatchMode()) + DELIMITER;
        line += training.getSettings().getHiddenNeurons() + DELIMITER;
        line += training.getSettings().getDecreaseFactor() + DELIMITER;
        line += training.getSettings().getIncreaseFactor() + DELIMITER;
        line += training.getSettings().getInitialDelta() + DELIMITER;
        line += training.getSettings().getMaxDelta() + DELIMITER;
        line += training.getSettings().getMinDelta() + DELIMITER + "\n";
        
        return line;
    }
/**
 * Method for generating header for csv file
 * @return 
 */
    private static String writeheader() {
        String header = "";
        header += "Algorithm" + DELIMITER;
        header += "Minimum iterations" + DELIMITER;
        header += "Maximum iterations" + DELIMITER;
        header += "Mean of iterations" + DELIMITER;
        header += "Standard deviation of iterations" + DELIMITER;
        header += "Minimum total error" + DELIMITER;
        header += "Maximum total error" + DELIMITER;
        header += "Mean of total error" + DELIMITER;
        header += "Standard deviation of total error" + DELIMITER;
        header += "Learning rate" + DELIMITER;
        header += "Momentum" + DELIMITER;
        header += "Maximum iterations" + DELIMITER;
        header += "Maximum error" + DELIMITER;
        header += "Batch mode" + DELIMITER;
        header += "Hidden neurons" + DELIMITER;
        header += "Decrease factor" + DELIMITER;
        header += "Increase factor" + DELIMITER;
        header += "Initial delta" + DELIMITER;
        header += "Maximum delta" + DELIMITER;
        header += "Minimum delta" + DELIMITER + "\n";
       
        return header;
    }
}
