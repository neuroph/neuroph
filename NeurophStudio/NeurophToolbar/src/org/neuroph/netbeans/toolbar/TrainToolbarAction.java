/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.core.learning.DataSet;
import org.neuroph.netbeans.main.ViewManager;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTraining;
import org.neuroph.netbeans.main.easyneurons.dialog.BackpropagationTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.HebbianTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.SupervisedTrainingDialog;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.NeuralNetworkType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "org.neuroph.netbeans.toolbar.TrainToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/calculate.png",
        displayName = "#CTL_TrainToolbarAction")
@ActionReference(path = "Toolbars/File", position = -700)
@Messages("CTL_TrainToolbarAction=Train")
public final class TrainToolbarAction implements ActionListener,  LookupListener {

    Lookup.Result<DataSet> trainingResultSets;
    DataSet trainingSet;
    NeuralNetworkTraining trainingController;
    NeuralNetwork nnet;
    ViewManager easyNeuronsViewController;

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        Lookup global = Utilities.actionsGlobalContext();
        nnet = global.lookup(NeuralNetwork.class);
        if (nnet != null) {
            trainingController = new NeuralNetworkTraining(nnet);
            TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
            trainingResultSets = projWindow.getLookup().lookupResult(DataSet.class);
            trainingResultSets.addLookupListener(this);
            resultChanged(new LookupEvent(trainingResultSets));
            train();
        }
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result r = (Lookup.Result) le.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            trainingSet = (DataSet) c.iterator().next(); 
        }
    }

    /*@Override
    public void handleNeuralNetworkEvent(NeuralNetworkEvent nne) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public void train() {
        if (trainingSet != null) {
            trainingController.setTrainingSet(trainingSet);
            
            NeuralNetworkType nnetType = nnet.getNetworkType(); 

            switch (nnetType) {
                case ADALINE:
                    showLmsTrainingDialog();
                    break;
                case PERCEPTRON:
                    showLmsTrainingDialog(); // perceptronTraining(); 
                    break;
                case MULTI_LAYER_PERCEPTRON:
                    showMLPTrainingDialog();
                    break;
                case RBF_NETWORK:
                    showLmsTrainingDialog(); // showRbfTrainingDialog 
                    break;
                case HOPFIELD:
                    trainingController.train();
                    break;
                case KOHONEN: // KohonenTrainDlg(); 
                    break;
                case NEURO_FUZZY_REASONER:
                    showLmsTrainingDialog();
                    break;
                case SUPERVISED_HEBBIAN_NET:
                    showHebbianTrainingDialog();
                    break;

                default:
                    trainingController.train();
                    break;
            } // switch
        }
    }

    private void showLmsTrainingDialog() {
        SupervisedTrainingDialog trainingDialog = new SupervisedTrainingDialog(null, true, this.trainingController);
        trainingDialog.setLocationRelativeTo(null);
        trainingDialog.setVisible(true);
    }

    private void showMLPTrainingDialog() {
        if (trainingController.getNetwork().getLearningRule() instanceof DynamicBackPropagation) {
            BackpropagationTrainingDialog trainingDialog = new BackpropagationTrainingDialog(null, easyNeuronsViewController, true,
                    this.trainingController);
            trainingDialog.setLocationRelativeTo(null);
            trainingDialog.setVisible(true);
        } else {
            showLmsTrainingDialog();
        }
    }

    private void showHebbianTrainingDialog() {
        HebbianTrainingDialog trainingDialog = new HebbianTrainingDialog(null,
                true, easyNeuronsViewController, this.trainingController);
        trainingDialog.setLocationRelativeTo(null);
        trainingDialog.setVisible(true);
    }

   
}
