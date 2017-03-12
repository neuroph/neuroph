package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.main.easyneurons.TestTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author Maja
 */
@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.toolbar.TestToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/test.png",
        displayName = "#CTL_TestToolbarAction")
@ActionReference(path = "Toolbars/Neuroph", position = -700)
@NbBundle.Messages("CTL_TestToolbarAction=Test")
public class TestToolbarAction implements ActionListener {

    private final NeuralNetAndDataSet trainingController;
    
    public TestToolbarAction(NeuralNetAndDataSet context) {
        this.trainingController = context;
    }    

    @Override
    public void actionPerformed(ActionEvent e) {

        TestTopComponent.getDefault().open();
        TestTopComponent.getDefault().requestActive();
        TestTopComponent.getDefault().clear();
             /*      
        // ovo ubaciti u classifier evaluation
        // napraviti sve tako da se iz dataseta uzimaju labele klasa (labele outputa)
        // napraviti kompletan handy workflow za micr scenario 
        
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Testing neural network "+trainingController.getNetwork().getLabel() +" for data set "+trainingController.getDataSet().getLabel());        

        Evaluation evaluation = new Evaluation();
        evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
        evaluation.addEvaluator(new ClassificationEvaluator.MultiClass(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
    
        evaluation.evaluateDataSet(trainingController.getNetwork(), trainingController.getDataSet());
        
        TestTopComponent.getDefault().output("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult()+"\r\n");
                
        ClassificationEvaluator evaluator = evaluation.getEvaluator(ClassificationEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getConfusionMatrix();  
        
        TestTopComponent.getDefault().output("Confusion matrrix:\r\n");
        TestTopComponent.getDefault().output(confusionMatrix.toString()+"\r\n\r\n");
       
        TestTopComponent.getDefault().output("Classification metrics\r\n");
        ClassificationMetrics[] metrics = evaluator.getResult();      
        for(ClassificationMetrics cm : metrics)
            TestTopComponent.getDefault().output(cm.toString());   
        */
        
       
        double totalMSE = 0;

        Iterator<DataSetRow> iterator = trainingController.getDataSet().iterator();
        while (iterator.hasNext()) {
            DataSetRow trainingEl = iterator.next();
            double[] inputs = trainingEl.getInput();
            trainingController.getNetwork().setInput(inputs);
            trainingController.getNetwork().calculate();
            double[] outputs = trainingController.getNetwork().getOutput();

            double desiredOutputs[] = trainingEl.getDesiredOutput();
            double errors[] = new double[outputs.length];
            double patternError = 0;
            for (int i = 0; i < outputs.length; i++) {
                errors[i] = outputs[i] - desiredOutputs[i];
                patternError += errors[i] * errors[i];
            }
            patternError = patternError / errors.length;

            String outputStr = "Input: " + arrayToString(inputs) + " Output: " + arrayToString(outputs) + "Desired output: " + arrayToString(desiredOutputs) + " Error: " + arrayToString(errors) + "\r\n";

            TestTopComponent.getDefault().output(outputStr);
            totalMSE += patternError;
        }

        totalMSE = totalMSE / trainingController.getDataSet().size();

        TestTopComponent.getDefault().output("Total Mean Square Error: " + totalMSE);
            
      
    }

    private String arrayToString(double[] arr) {
        StringBuilder result = new StringBuilder();
        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(4);

        for (int i = 0; i < arr.length; i++) {
            result.append(numberFormat.format(arr[i]) + "; ");
        }

        return result.toString();
    }
}
