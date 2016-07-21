package org.neuroph.netbeans.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.SwingUtilities;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.transfer.Trapezoid;
import org.neuroph.netbeans.classificationsample.MultiLayerPerceptronVisualizationTopComponent;
import org.neuroph.netbeans.classificationsample.NeuralNetObserver;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.main.easyneurons.DataSetTopComponent;
import org.neuroph.netbeans.main.easyneurons.dialog.SupervisedTrainingMonitorTopComponent;
import org.neuroph.netbeans.main.easyneurons.errorgraph.GraphFrameTopComponent;
import org.neuroph.netbeans.main.easyneurons.samples.KohonenSampleTopComponent;
import org.neuroph.netbeans.main.easyneurons.samples.NFRSampleTopComponent;
import org.neuroph.netbeans.classificationsample.ObservableTrainingSet;
import org.neuroph.netbeans.main.easyneurons.samples.perceptron.TrainingSetObserver;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.NeuroFuzzyPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author user
 */
public class ViewManager implements
        Serializable {

    private static final long serialVersionUID = 1L;
    private static ViewManager instance;

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    private ViewManager() {
    }

    public void onNetworkClose(NeuralNetwork nnet) {
        // openedNetworks.remove(nnet);
    }

    public void onTrainingSetClose(DataSet tset) {
        // openedTrainingSets.remove(tset);
    }

    /**
     * Opens TrainigSetEditFrameTopComponent - opened by double clicking on
     * training set
     *
     * @param trainingSet - input trainig set that will be edited
     */
//    public void openTrainingSetWindow(DataSet trainingSet) {
////        DataSetTopComponent trainingSetTopComponent;
////
////        if (openedTrainingSets.containsKey(trainingSet)) {
////            trainingSetTopComponent = openedTrainingSets.get(trainingSet); // if network is allready opened get the window
////            trainingSetTopComponent.requestActive();
////        } else {
////            trainingSetTopComponent = new DataSetTopComponent(trainingSet); // otherwise create new window to open network in
////            //  trainingSetTopComponent.setTrainingSetEditFrameVariables(trainingSet);
////            trainingSetTopComponent.open();
////            openedTrainingSets.put(trainingSet, trainingSetTopComponent);
////            trainingSetTopComponent.requestActive();
////        }
//
//    }

    /**
     * Opens TrainingEditFrameTopComponent - opened by TrainingSet Wizard
     *
     * @param inputs - number of inputs
     * @param outputs - number of outputs
     * @param type - type of trainig set
     * @param label - label name of training set
     */
    public void showTrainingSetEditFrame(int inputs, int outputs, String type, String label) {
        DataSet dataSet = new DataSet(inputs, outputs);
        dataSet.setLabel(label);
        DataSetTopComponent trainingSetTopComponent = new DataSetTopComponent();
        trainingSetTopComponent.setTrainingSetEditFrameVariables(dataSet, type, inputs, outputs);
        trainingSetTopComponent.open();
        trainingSetTopComponent.requestActive();
    }

    public void showTrainingSetEditFrame(DataSet dataSet, int inputs, int outputs, String type, String label) {
        DataSetTopComponent trainingSetTopComponent = new DataSetTopComponent();
        trainingSetTopComponent.setTrainingSetEditFrameVariables(dataSet, type, inputs, outputs);
        trainingSetTopComponent.open();
        trainingSetTopComponent.requestActive();
    }

    // this should go to training controller
    public void openTrainingMonitorWindow(NeuralNetAndDataSet trainingController) {
        SupervisedTrainingMonitorTopComponent monitorWindow = SupervisedTrainingMonitorTopComponent.findInstance();
        monitorWindow.setSupervisedTrainingMonitorFrameVariables(trainingController);
        monitorWindow.open();
        monitorWindow.requestActive();
        monitorWindow.observe(trainingController.getNetwork().getLearningRule());
    }

    /**
     * Opens ErrorGraphTopComponent
     *
     * @return
     */
    public GraphFrameTopComponent openErrorGraphFrame() {
        GraphFrameTopComponent graphFrame = new GraphFrameTopComponent();
        graphFrame.open();
        graphFrame.requestActive();
        return graphFrame;
    }

    /**
     * Opens Kohonen sample
     */
    public void kohonenSample() {
        int sampleSize = 100;
        NeuralNetwork neuralNet = new Kohonen(new Integer(2), new Integer(sampleSize));
        neuralNet.setLabel("KohonenNet");
        DataSet dataSet = new DataSet(2);
        dataSet.setLabel("Sample training set");

        for (int i = 0; i < sampleSize; i++) {
            ArrayList<Double> trainVect = new ArrayList<Double>();
            trainVect.add(Math.random());
            trainVect.add(Math.random());
            DataSetRow te = new DataSetRow(trainVect);
            dataSet.addRow(te);
        }

        NeuralNetAndDataSet controller = new NeuralNetAndDataSet(neuralNet, dataSet);

        KohonenSampleTopComponent kohonenVisualizer = new KohonenSampleTopComponent();
        kohonenVisualizer.setNeuralNetworkTrainingController(controller);
        neuralNet.getLearningRule().addListener(kohonenVisualizer);

        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNet);
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(dataSet);

        kohonenVisualizer.setVisible(true);
        kohonenVisualizer.open();
        kohonenVisualizer.requestActive();
    }

    public void licencePlateRecognitionSample() {
       TopComponent tc = WindowManager.getDefault().findTopComponent("LprTopComponent");
        tc.open();                  
    }

    /**
     * Opens NFRSample
     */
    public void nfrSample() {
        double[][] pointsSets = {{0, 0, 20, 22}, // bad
        {20, 22, 40, 42}, // good
        {40, 42, 80, 82}, // very good
        {80, 82, 100, 100}}; // excellent

        double[][] timeSets = {{15, 15, 20, 25}, // fast
        {20, 25, 35, 40}, // moderate
        {35, 40, 1000, 1000}}; // slow

        NeuralNetwork nnet = new NeuroFuzzyPerceptron(pointsSets, timeSets);
        DataSet tSet = new DataSet(2, 4);

        Layer setLayer = nnet.getLayerAt(1);

        int outClass = 0;

        for (int i = 0; i <= 3; i++) { // iterate points sets
            Neuron icell = setLayer.getNeuronAt(i);
            Trapezoid tfi = (Trapezoid) icell.getTransferFunction();
            double r1i = tfi.getRightLow();
            double l2i = tfi.getLeftHigh();
            double r2i = tfi.getRightHigh();
            double right_intersection_i = r2i + (r1i - r2i) / 2;

            for (int j = 6; j >= 4; j--) { // iterate speed sets
                Neuron jcell = setLayer.getNeuronAt(j);
                Trapezoid tfj = (Trapezoid) jcell.getTransferFunction();

                double r1j = tfj.getRightLow();
                double l2j = tfj.getLeftHigh();
                double r2j = tfj.getRightHigh();
                double right_intersection_j = r2j + (r1j - r2j) / 2;

                String outputPattern;
                if (outClass <= 3) {
                    outputPattern = "1 0 0 0";
                } else if ((outClass >= 4) && (outClass <= 6)) {
                    outputPattern = "0 1 0 0";
                } else if ((outClass >= 7) && (outClass <= 9)) {
                    outputPattern = "0 0 1 0";
                } else {
                    outputPattern = "0 0 0 1";
                }

                String inputPattern = Double.toString(l2i) + " "
                        + Double.toString(l2j);
                DataSetRow tEl = new DataSetRow(
                        inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(l2i) + " "
                        + Double.toString(r2j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(l2i) + " "
                        + Double.toString(right_intersection_j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(r2i) + " "
                        + Double.toString(l2j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(r2i) + " "
                        + Double.toString(r2j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(r2i) + " "
                        + Double.toString(right_intersection_j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(right_intersection_i) + " "
                        + Double.toString(l2j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(right_intersection_i) + " "
                        + Double.toString(r2j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                inputPattern = Double.toString(right_intersection_i) + " "
                        + Double.toString(right_intersection_j);
                tEl = new DataSetRow(inputPattern, outputPattern);
                tSet.addRow(tEl);

                outClass++;
            } // for j

        } // for i

        nnet.setLabel("NFR sample");
        tSet.setLabel("NFR tset");

        NeuralNetAndDataSet controller = new NeuralNetAndDataSet(nnet, tSet);

        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(nnet);
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(tSet);

        NFRSampleTopComponent frame = new NFRSampleTopComponent();
        frame.setNeuralNetworkTrainingController(controller);

        frame.open();
        frame.requestActive();
    }

    /**
     * Opens PerceptronSample
     */
    public void showPerceptronSample() {
        org.neuroph.netbeans.main.easyneurons.samples.perceptron.PerceptronSampleFrameTopComponent perceptronVisualizer = new org.neuroph.netbeans.main.easyneurons.samples.perceptron.PerceptronSampleFrameTopComponent();
        perceptronVisualizer.initSample(); // sve odavde prebaciti u top coponent svu inicjijalizaciju kreiranje nn i ts i ocistiti i sve ostalo iz ove metodw!!!!!!
        perceptronVisualizer.open();
        perceptronVisualizer.requestActive();
    }

    public void showMultiLayerPerceptronSample() {
        NeuralNetObserver neuralNetObserver = new NeuralNetObserver() {
            @Override
            public void update(Observable o, Object arg) {
                NeuralNetwork net = getNnet();

                super.update(o, arg);
                net = getNnet();
            }
        };

        TrainingSetObserver trainingSetObserver = new TrainingSetObserver() {
            @Override
            public void update(Observable o, Object arg) {
                super.update(o, arg);
            }
        };

        final ObservableTrainingSet observableTrainingSet = new ObservableTrainingSet();
        observableTrainingSet.addObserver(trainingSetObserver);
        observableTrainingSet.addObserver(neuralNetObserver);

        NeuralNetwork nnet = neuralNetObserver.getNnet();
        nnet.setLearningRule(new BackPropagation());       //ovo je moralo da se stavi... zasto - treba izbaciti i staviti momentum

        SwingUtilities.invokeLater(new Runnable() {
            // pozovi ovo iz wizarda
            public void run() {
                MultiLayerPerceptronVisualizationTopComponent backpropagationVisualizer = new MultiLayerPerceptronVisualizationTopComponent();
                backpropagationVisualizer.setTrainingSetForMultiLayerPerceptronSample(observableTrainingSet);

                backpropagationVisualizer.setVisible(true);
                backpropagationVisualizer.open();

                backpropagationVisualizer.requestActive();
            }
        }
        );
        
        //showMessage("Started Multi Layer Perceptron with Backpropagation Sample");

    }
}
