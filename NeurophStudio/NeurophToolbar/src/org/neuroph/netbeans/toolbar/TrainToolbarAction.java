package org.neuroph.netbeans.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.dev.noprop.NoPropNet;
import org.neuroph.dev.noprop.RangeNormalizer;
import org.neuroph.netbeans.classificationsample.InputSettngsDialog;
import org.neuroph.netbeans.visual.TrainingController;
import org.neuroph.netbeans.main.ViewManager;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.main.easyneurons.dialog.BackpropagationTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.HebbianTrainingDialog;
import org.neuroph.netbeans.main.easyneurons.dialog.SupervisedTrainingDialog;
import org.neuroph.netbeans.classificationsample.MultiLayerPerceptronClassificationSamplePanel;
import org.neuroph.netbeans.classificationsample.MultiLayerPerceptronClassificationSampleTopComponent;
import org.neuroph.netbeans.classificationsample.Combinatorics;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.NeuroFuzzyPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.data.norm.Normalizer;
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
    private TrainingController trainingController;
    ViewManager easyNeuronsViewController;

    public TrainToolbarAction(NeuralNetAndDataSet context) {
        this.neuralNetAndDataSet = context;
        trainingController = new TrainingController(neuralNetAndDataSet);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        train();
    }

    public void ClassificationSampleModuleCheck() {
        MultiLayerPerceptronClassificationSampleTopComponent mlp = MultiLayerPerceptronClassificationSamplePanel.mlpSampleTc;
        if (mlp != null) {
            if (mlp.isTrainSignal()) {
                mlp.visualizationPreprocessing();
                mlp.setVisualizationStarted(true);
                mlp.setDrawingLocked(true);
                int[] storedInputs = InputSettngsDialog.getInstance().getStoredInputs();
                if (mlp.getInputSpacePanel().positiveInputsOnly()) {
                    mlp.generateSetValues(57, 0.0357142857142857/2.0);
                } else {
                    mlp.generateSetValues(57, 0.0357142857142857);
                }
                mlp.setInputs(Combinatorics.Variations.generateVariations(mlp.getSetValues(), neuralNetAndDataSet.getNetwork().getInputsCount(), true));
                mlp.setStoredInputs(storedInputs);
                if (MultiLayerPerceptronClassificationSamplePanel.SHOW_POINTS && mlp.isAllPointsRemoved()
                        || mlp.isPointDrawed()) {
                    try {
                        mlp.drawPointsFromTrainingSet(neuralNetAndDataSet.getDataSet(), storedInputs);
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
                ClassificationSampleModuleCheck();
            } else if (neuralNetClass.equals(SupervisedHebbianNetwork.class)) {
                showHebbianTrainingDialog();
            } else if (neuralNetClass.equals(NoPropNet.class)) {
                
                neuralNetAndDataSet.getNetwork().randomizeWeights(new RangeRandomizer(-1, 1));
                RangeNormalizer norm = new RangeNormalizer(-0.9, 0.9);
                norm.normalize(neuralNetAndDataSet.getDataSet());
                neuralNetAndDataSet.getDataSet().shuffle();
                showLmsTrainingDialog();
            } else {
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
        SupervisedTrainingDialog trainingDialog = new SupervisedTrainingDialog(null, true, this.neuralNetAndDataSet);
        trainingDialog.setLocationRelativeTo(null);
        trainingDialog.setVisible(true);
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
