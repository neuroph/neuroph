package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.visual.VisualEditorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.IOProvider;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.toolbar.RandomizeToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/randomize.png",
        displayName = "#CTL_RandomizeToolbarAction")
@ActionReference(path = "Toolbars/Neuroph", position = -500)
@Messages("CTL_RandomizeToolbarAction=Randomize")
public final class RandomizeToolbarAction implements ActionListener {

    private final NeuralNetwork context;  
    
    public RandomizeToolbarAction(NeuralNetwork context) {
        this.context = context;
    }       
    
    @Override
    public void actionPerformed(ActionEvent e) {
        NeuralNetAndDataSet training = new NeuralNetAndDataSet(context);
        training.randomize();
        
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Weights randomized for network "+context.getLabel());                
        
        TopComponent graph = TopComponent.getRegistry().getActivated();
       if (graph instanceof VisualEditorTopComponent) {
            ((VisualEditorTopComponent) graph).refresh();
        }
    }
}
