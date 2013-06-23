package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.netbeans.main.ViewManager;
import org.neuroph.netbeans.visual.NeuralNetworkTraining;
import org.neuroph.netbeans.visual.TrainingManager;
import org.neuroph.netbeans.main.easyneurons.dialog.BackpropagationTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.HebbianTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.SupervisedTrainingDialog;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.NeuralNetworkType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Neuroph",
        id = "org.neuroph.netbeans.toolbar.TrainToolbarAction")
@ActionRegistration(
        iconBase = "org/neuroph/netbeans/toolbar/icons/train.png",
        displayName = "#CTL_TrainToolbarAction")
@ActionReference(path = "Toolbars/Neuroph", position = -800)
@Messages("CTL_TrainToolbarAction=Train")
public final class TrainToolbarAction implements ActionListener {

    private final NeuralNetworkTraining trainingController;
    ViewManager easyNeuronsViewController;

    public TrainToolbarAction(NeuralNetworkTraining context) {
        this.trainingController = context;
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
//        Lookup global = Utilities.actionsGlobalContext();
//        nnet = global.lookup(NeuralNetwork.class);
//        if (nnet != null) {
//            trainingController = new NeuralNetworkTraining(nnet);
//            TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
//            trainingResultSets = projWindow.getLookup().lookupResult(DataSet.class);
//            trainingResultSets.addLookupListener(this);
//            resultChanged(new LookupEvent(trainingResultSets));
//            train();
//        }
      //  trainingController = TrainingManager.getDefault().getTraining();
        train();

    }

    /*@Override
     public void handleNeuralNetworkEvent(NeuralNetworkEvent nne) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }*/
    public void train() {
        if (trainingController.getTrainingSet() != null) {
            trainingController.setTrainingSet(trainingController.getTrainingSet());

            NeuralNetworkType nnetType = trainingController.getNetwork().getNetworkType();

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
