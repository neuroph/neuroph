package org.neuroph.netbeans.classifier.eval;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.goai.classification.samples.NeurophClassifierEvaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.main.easyneurons.TestTopComponent;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
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
    
    private final NeuralNetAndDataSet trainingController;
    
    public ClassifierTestAction(NeuralNetAndDataSet context) {
        this.trainingController = context;
    }        

    @Override
    public void actionPerformed(ActionEvent e) {
        TestTopComponent.getDefault().open();
        TestTopComponent.getDefault().requestActive();
        TestTopComponent.getDefault().clear();
        
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Evaluating classifier neural network "/*+trainingController.getNetwork().getLabel() +" for data set "/*+trainingController.getDataSet().getLabel()*/);        

       NeuralNetwork neuralNet = trainingController.getNetwork();
       DataSet dataSet = trainingController.getDataSet();
       
       // just run GOAI classifier evaluat here and put results in TC!
        NeurophClassifierEvaluation evaluation = 
                new NeurophClassifierEvaluation(neuralNet, dataSet); 
        
    //    String[] classNames = {"Setosa", "Versicolor", "Virginica"}; // these shoul dbe set either from output neurons or data set...        
       String[] classNames =  new String[neuralNet.getOutputsCount()];// = {"LeftHand", "RightHand", "Foot", "Rest"};        
       int i = 0;
       for(Neuron n : neuralNet.getOutputNeurons()) {
           classNames[i] = n.getLabel(); 
           i++;
       } 
       
       evaluation.setClassNames(classNames);
       evaluation.run();
        
       TestTopComponent.getDefault().output("Classifier evaluation results: \r\n"+evaluation.getEvaluationResults().toString());          
        ClassifierChartTopComponent tcc = new ClassifierChartTopComponent();
        tcc.setEvaluationResult(evaluation.getEvaluationResults());
 
        tcc.open();
        tcc.requestActive();
       
    }
}
