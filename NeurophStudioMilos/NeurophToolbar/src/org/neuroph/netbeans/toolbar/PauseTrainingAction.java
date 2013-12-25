package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.netbeans.visual.TrainingController;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.toolbar.PauseTrainingAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/pause24.png",
        displayName = "#CTL_PauseTrainingAction")
@ActionReference(path = "Toolbars/Neuroph", position = -725)
@Messages("CTL_PauseTrainingAction=Pause")
public final class PauseTrainingAction implements ActionListener {

    private TrainingController trainingController;
 
    
    
    public PauseTrainingAction(TrainingController context) {
        this.trainingController = context;
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        PrintWriter out = io.getOut();

        int currentIteration = ((IterativeLearning)trainingController.getNeuralNetAndDataSet().getNetwork().getLearningRule()).getCurrentIteration();  ;        
        
        if (!trainingController.isPaused()) {
            trainingController.pause();
            out.println("Training paused at "+currentIteration+" iteration");
            // chang ebutton icon to resume image
        } else {
            trainingController.resume();
            out.println("Training resumed at "+currentIteration+" iteration");
        }
    }
}