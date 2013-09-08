package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.netbeans.main.easyneurons.errorgraph.GraphFrameTopComponent;
import org.neuroph.netbeans.visual.TrainingController;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.toolbar.StopTrainingAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/stop24.png",
        displayName = "#CTL_StopTrainingAction")
@ActionReference(path = "Toolbars/Neuroph", position = -750)
@Messages("CTL_StopTrainingAction=Stop")
public final class StopTrainingAction implements ActionListener {

    private final TrainingController trainingController;    

    public StopTrainingAction(TrainingController context) {
        this.trainingController = context;
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        trainingController.stop();

        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        PrintWriter out = io.getOut();
        out.println("Training stoped");  
  //      GraphFrameTopComponent.getDefault().handleLearningEvent(new LearningEvent(trainingController.getNeuralNetAndDataSet().getNetwork().getLearningRule()));
    }
}
