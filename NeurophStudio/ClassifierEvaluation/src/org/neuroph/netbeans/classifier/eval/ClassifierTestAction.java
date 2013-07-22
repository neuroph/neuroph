/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.classifier.eval;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.goai.classification.samples.NeurophClassifierEvaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.netbeans.main.easyneurons.TestTopComponent;
import org.neuroph.netbeans.visual.NeuralNetworkTraining;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(
        category = "Tools",
        id = "org.neuroph.netbeans.classifier.eval.ClassifierTestAction")
@ActionRegistration(
        displayName = "#CTL_ClassifierTestAction")
@ActionReference(path = "Menu/Tools", position = 100)
@Messages("CTL_ClassifierTestAction=Classifier Test ")
public final class ClassifierTestAction implements ActionListener {
    
    private final NeuralNetworkTraining trainingController;
    
    public ClassifierTestAction(NeuralNetworkTraining context) {
        this.trainingController = context;
    }        

    @Override
    public void actionPerformed(ActionEvent e) {
        TestTopComponent.getDefault().open();
        TestTopComponent.getDefault().requestActive();
        TestTopComponent.getDefault().clear();
        
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Evaluating classifier neural network "/*+trainingController.getNetwork().getLabel() +" for data set "/*+trainingController.getTrainingSet().getLabel()*/);        

       NeuralNetwork neuralNet = trainingController.getNetwork();
       DataSet dataSet = trainingController.getTrainingSet();
       
       // just run GOAI classifier evaluat here and put results in TC!
        NeurophClassifierEvaluation evaluation = 
                new NeurophClassifierEvaluation(neuralNet, dataSet); 
        
        String[] classNames = {"Setosa", "Versicolor", "Virginica"}; // these shoul dbe set either from output neurons or data set...        
        evaluation.setClassNames(classNames);
        
        evaluation.run();
        
       TestTopComponent.getDefault().output(evaluation.getEvaluationResults().toString());          
 
       
    }
}
