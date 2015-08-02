package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.dev.noprop.NoPropNet;

import org.neuroph.netbeans.classificationsample.InputSettngsDialog;
import org.neuroph.netbeans.visual.TrainingController;
import org.neuroph.netbeans.main.ViewManager;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.main.easyneurons.dialog.BackpropagationTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.HebbianTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.SupervisedTrainingDialog;
import org.neuroph.netbeans.classificationsample.MultiLayerPerceptronClassificationSamplePanel;
import org.neuroph.netbeans.classificationsample.MultiLayerPerceptronVisualizationTopComponent;
import org.neuroph.netbeans.classificationsample.Combinatorics;
import org.neuroph.netbeans.main.easyneurons.dialog.NewSupervisedTrainingDialog;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.NeuroFuzzyPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.data.norm.RangeNormalizer;
import org.neuroph.util.random.RangeRandomizer;
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

    private final NeuralNetAndDataSet neuralNetAndDataSet;
  //  private TrainingController trainingController;
    ViewManager easyNeuronsViewController;

    public TrainToolbarAction(NeuralNetAndDataSet context) {
        this.neuralNetAndDataSet = context;
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        train();
    }

    public void classificationSampleModuleCheck() {
        MultiLayerPerceptronVisualizationTopComponent mlpSample =  MultiLayerPerceptronClassificationSamplePanel.mlpSampleTc;
        if (mlpSample != null) {
            if (mlpSample.isTrainSignal()) {
                mlpSample.visualizationPreprocessing();
                mlpSample.setVisualizationStarted(true);
                mlpSample.setDrawingLocked(true);
               
                int[] storedInputs = InputSettngsDialog.getInstance().getStoredInputs();
                if (mlpSample.getVisualizationPanel().positiveInputsOnly()) {
                    mlpSample.generateSetValues(80, 0.025316456/2.0); //this was 57
                } else {
                    //mlpSample.generateSetValues(57, 0.0357142857142857); //this 
                    mlpSample.generateSetValues(80, 0.025316456); //this                                                                                                
                }
                
                mlpSample.setInputs(Combinatorics.Variations.generateVariations(mlpSample.getSetValues(), neuralNetAndDataSet.getNetwork().getInputsCount(), true));
                mlpSample.setStoredInputs(storedInputs);
                
                if (MultiLayerPerceptronClassificationSamplePanel.SHOW_POINTS && mlpSample.isAllPointsRemoved()
                        || mlpSample.isPointDrawed()) {
                    try {
                        mlpSample.drawPointsFromTrainingSet(neuralNetAndDataSet.getDataSet(), storedInputs);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void train() {
        if (neuralNetAndDataSet.getDataSet() != null) {
            neuralNetAndDataSet.setDataSet(neuralNetAndDataSet.getDataSet()); // ???????

            Class neuralNetClass = neuralNetAndDataSet.getNetwork().getClass();

            if (neuralNetClass.equals(Adaline.class)
                    || neuralNetClass.equals(Perceptron.class)
                    || neuralNetClass.equals(RBFNetwork.class)
                    || neuralNetClass.equals(NeuroFuzzyPerceptron.class)) {
                showLmsTrainingDialog();
            } else if (neuralNetClass.equals(MultiLayerPerceptron.class)) {
                showMLPTrainingDialog();
                classificationSampleModuleCheck();
            } else if (neuralNetClass.equals(SupervisedHebbianNetwork.class)) {
                showHebbianTrainingDialog();
            } else if (neuralNetClass.equals(NoPropNet.class)) {
                
                neuralNetAndDataSet.getNetwork().randomizeWeights(new RangeRandomizer(-1, 1));
                RangeNormalizer norm = new RangeNormalizer(-0.9, 0.9);
                norm.normalize(neuralNetAndDataSet.getDataSet());
                neuralNetAndDataSet.getDataSet().shuffle();
                showLmsTrainingDialog();
            } else {
                TrainingController trainingController = new TrainingController(neuralNetAndDataSet);
                trainingController.train();
            }

//            switch (neuralNetClass) {
//                case Adaline.class.toString():
//                    showLmsTrainingDialog();
//                    break;
//                case PERCEPTRON:
//                    showLmsTrainingDialog(); // perceptronTraining(); 
//                    break;
//                case MULTI_LAYER_PERCEPTRON:
//                    showMLPTrainingDialog();
//                    break;
//                case RBF_NETWORK:
//                    showLmsTrainingDialog(); // showRbfTrainingDialog 
//                    break;
//                case HOPFIELD:
//                    
//                    trainingController.train();
//                    break;
//                case KOHONEN: // KohonenTrainDlg(); 
//                    break;
//                case NEURO_FUZZY_REASONER:
//                    showLmsTrainingDialog();
//                    break;
//                case SUPERVISED_HEBBIAN_NET:
//                    showHebbianTrainingDialog();
//                    break;
//
//                default:
//                    trainingController.train();
//                    break;
//            } // switch
        }
    }

    private void showLmsTrainingDialog() {
        NewSupervisedTrainingDialog dialog = new NewSupervisedTrainingDialog(null, true, this.neuralNetAndDataSet);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showMLPTrainingDialog() {
        if (neuralNetAndDataSet.getNetwork().getLearningRule() instanceof DynamicBackPropagation) {
            BackpropagationTrainingDialog trainingDialog = new BackpropagationTrainingDialog(null, easyNeuronsViewController, true,
                    this.neuralNetAndDataSet);
            trainingDialog.setLocationRelativeTo(null);
            trainingDialog.setVisible(true);
        } else {
            showLmsTrainingDialog();
        }
    }

    private void showHebbianTrainingDialog() {
        HebbianTrainingDialog trainingDialog = new HebbianTrainingDialog(null,
                true, easyNeuronsViewController, this.neuralNetAndDataSet);
        trainingDialog.setLocationRelativeTo(null);
        trainingDialog.setVisible(true);
    }
}
