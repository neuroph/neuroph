package org.neuroph.netbeans.classifier.eval;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.MeanSquaredError;
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
    
    private final NeuralNetAndDataSet neuralNetAndDataSet;
    
    public ClassifierTestAction(NeuralNetAndDataSet context) {
        this.neuralNetAndDataSet = context;
    }        

    @Override
    public void actionPerformed(ActionEvent e) {
        TestTopComponent.getDefault().open();
        TestTopComponent.getDefault().requestActive();
        TestTopComponent.getDefault().clear();
        
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Evaluating classifier neural network "/*+neuralNetAndDataSet.getNetwork().getLabel() +" for data set "/*+neuralNetAndDataSet.getDataSet().getLabel()*/);        

       NeuralNetwork<?> neuralNet = neuralNetAndDataSet.getNetwork();
       DataSet dataSet = neuralNetAndDataSet.getDataSet();
       
       boolean isMultiClass = false;
   
       String[] classNames =  new String[neuralNet.getOutputsCount()]; // neuralNet.getOutputLabels()  
       int i = 0;
       
       for(Neuron n : neuralNet.getOutputNeurons()) {
           classNames[i] = n.getLabel(); 
           i++;
       } 

       if (i>1) isMultiClass = true;
       
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Testing neural network "+neuralNetAndDataSet.getNetwork().getLabel() +" for data set "+neuralNetAndDataSet.getDataSet().getLabel());        

        Evaluation evaluation = new Evaluation();
        evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
        
        if (isMultiClass) {
            evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(classNames));
        } else {        
            evaluation.addEvaluator(new ClassifierEvaluator.Binary(0.5));
        }
    
        evaluation.evaluateDataSet(neuralNetAndDataSet.getNetwork(), neuralNetAndDataSet.getDataSet());
        
        TestTopComponent.getDefault().output("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult()+"\r\n");
                
        ClassifierEvaluator evaluator;
        
        if (isMultiClass) {
            evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        } else {
            evaluator = evaluation.getEvaluator(ClassifierEvaluator.Binary.class);
        }
            
        
        ConfusionMatrix confusionMatrix = evaluator.getResult();  
        
        TestTopComponent.getDefault().output("Confusion matrrix:\r\n");
        TestTopComponent.getDefault().output(confusionMatrix.toString()+"\r\n\r\n");
       
        TestTopComponent.getDefault().output("Classification metrics\r\n");
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);      
   
        // calculate avg mse and avg metrics
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        
        for(ClassificationMetrics cm : metrics)
            TestTopComponent.getDefault().output(cm.toString()+"\r\n");    
        
        TestTopComponent.getDefault().output(average.toString());   
               
    }
}
