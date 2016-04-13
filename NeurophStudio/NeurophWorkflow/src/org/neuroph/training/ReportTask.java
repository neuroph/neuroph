package org.neuroph.training;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author zoran
 */
public class ReportTask extends Task {

    public ReportTask(String name) {
        super(name);
    }
       
    @Override
    public void execute() {
        // this method should generate training summary to copy paste to paper
          logMessage("Neural Network Training Report");
          logMessage("==============================");
          
          // get training statistics
          Stats iterationsStats = (Stats)getParentProcess().getVariable("iterationStats").getValue();
          Stats totalErrorStats = (Stats)getParentProcess().getVariable("totalErrorStats").getValue();
          
          logMessage(iterationsStats.toString());
          logMessage(totalErrorStats.toString());
          
          // and generate reporting tables i asciidoc
//        System.out.println(parentProcess.getProcessLog());  
    }
    
}
