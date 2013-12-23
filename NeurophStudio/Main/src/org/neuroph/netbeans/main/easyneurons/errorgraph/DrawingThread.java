/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.main.easyneurons.errorgraph;

import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.neuroph.netbeans.main.LearningInfo;
import org.openide.util.Exceptions;

/**
 *
 * @author zoran
 */
public class DrawingThread extends Thread {

    SimpleXYChartSupport chartSupport;
    LearningInfoBuffer buffer;
    long[] err = new long[1];
    boolean stop = false;
    private BufferedWriter buffWriter;

    public DrawingThread(LearningInfoBuffer buffer, SimpleXYChartSupport chartSupport, BufferedWriter buffWriter) {
        this.buffer = buffer;
        this.chartSupport = chartSupport;
        this.buffWriter = buffWriter;   
    }

    @Override
    public void run() {
        while (!stop) {
            LearningInfo learningInfo = buffer.get();
            int iteration = learningInfo.getIteration();
            double error = learningInfo.getError();

            if (iteration == -1) { // stop signall
                stop=true;
                continue;
            }

            // put them in chart
            err[0] = (long) (error * 1000); // ovo je bitna stvar je rgrf prikazuje long vrednosti
            chartSupport.addValues(System.currentTimeMillis(), err);
            chartSupport.updateDetails(new String[]{String.valueOf(iteration), learningInfo.getError().toString()});
            writeLearningDataToFile(iteration, error);
            try {
                Thread.sleep(10); // for smooth drawing
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }

        }

        try {
            buffWriter.flush();
            buffWriter.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        openJFreeChartInAThread();
        
   //     buffer.releaseThreads(); // ovo ne moze ovde -z zasto? gd etreba pa da obavestimo onog da ne ceka vise

    }

    private void writeLearningDataToFile(int iteration, double error) {
        String errorNumber = String.valueOf(error);
        String iterationNumber = String.valueOf(iteration);

        try {
            buffWriter.write(iterationNumber + "," + errorNumber);
            buffWriter.newLine();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void openJFreeChartInAThread() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFreeChartTopComponent graphFrame = new JFreeChartTopComponent();
                    graphFrame.open();
                    graphFrame.requestActive();
                }
            });

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
