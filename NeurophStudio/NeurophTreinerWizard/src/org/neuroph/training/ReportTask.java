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
          logMessage("Reporting task");
          // calculate statistics about the traing
          // and generate reporting tables i asciidoc
//        System.out.println(parentProcess.getProcessLog());  
//        InputOutput io = IOProvider.getDefault().getIO ("Training Report", true);
//        io.getOut().println (parentProcess.getProcessLog());
//        io.getOut().close();
    }
    
}
