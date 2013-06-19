/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.netbeans.main.easyneurons.TestTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Maja
 */
@ActionID(
        category = "File",
        id = "org.neuroph.netbeans.toolbar.TestToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/calculate.png",
        displayName = "#CTL_TestToolbarAction")
@ActionReference(path = "Toolbars/File", position = -700)
@NbBundle.Messages("CTL_TestToolbarAction=Test")
public class TestToolbarAction implements ActionListener, LookupListener {

    DataSet trainingSet;
    Lookup.Result<DataSet> trainingResultSets;
    NeuralNetwork nnet;

    @Override
    public void actionPerformed(ActionEvent e) {

        Lookup global = Utilities.actionsGlobalContext();
        nnet = global.lookup(NeuralNetwork.class);
        TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
        trainingResultSets = projWindow.getLookup().lookupResult(DataSet.class);
        trainingResultSets.addLookupListener(this);
        resultChanged(new LookupEvent(trainingResultSets));
        test();        
    }
    
    private void test(){
        TestTopComponent.getDefault().open();
        TestTopComponent.getDefault().requestActive();
        TestTopComponent.getDefault().clear();

        double totalMSE = 0;

        Iterator<DataSetRow> iterator = trainingSet.iterator();
        while (iterator.hasNext()) {
            DataSetRow trainingEl = iterator.next();
            double[] inputs = trainingEl.getInput();
            nnet.setInput(inputs);
            nnet.calculate();
            double[] outputs = nnet.getOutput();

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

        totalMSE = totalMSE / trainingSet.size();

        TestTopComponent.getDefault().output("Total Mean Square Error: " + totalMSE);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result r = (Lookup.Result) le.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            trainingSet = (DataSet) c.iterator().next();
        }
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
