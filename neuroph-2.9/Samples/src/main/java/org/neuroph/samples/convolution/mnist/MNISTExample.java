package org.neuroph.samples.convolution.mnist;

import org.neuroph.contrib.model.errorestimation.CrossValidation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Konvolucioni parametri
 * <p/>
 * Globalna arhitektura: Konvolucioni i pooling lejeri - naizmenicno (samo konvolucioni ili naizmenicno konvolccioni pooling)
 * Za svaki lajer da ima svoj kernel (mogu svi konvolucioni da imaju isti kernel, ili svi pooling isti kernel)
 * da mogu da zadaju neuron properties (transfer funkciju) za konvolucioni i pooling lejer(input)
 * Konektovanje lejera? - po defaultu full connect (ostaviti api za custom konekcije)
 * <p/>
 * addFeatureMaps...
 * connectFeatureMaps
 * <p/>
 * Helper utility klasa...
 * <p/>
 * Osnovni kriterijumi:
 * 1. Jednostavno kreiranje default neuronske mreze
 * 2. Laka customizacija i kreiranje custom arhitektura: konvolucionih i pooling lejera i transfer/input funkcija
 * Napraviti prvo API i ond aprilagodti kod
 * <p/>
 * ------------------------
 * <p/>
 * promeniti nacin kreiranja i dodavanja feature maps layera
 * resiti InputMaps Layer, overridovana metoda koja baca unsupported exception ukazuje da nesto nije u redu sa dizajnom
 * Da li imamo potrebe za klasom kernel  - to je isto kao i dimension?
 * <p/>
 * zasto je public abstract void connectMaps apstraktna? (u klasi FeatureMapsLayer)
 * <p/>
 * InputMapsLayer konstruktoru superklase prosledjuje null...
 * <p/>
 * fullConnectMapLayers
 *
 * 
 * Same as CNNMNIST just with hardoced params
 * 
 * @author zoran
 */


public class MNISTExample {

    private static Logger LOG = LoggerFactory.getLogger(MNISTExample.class);

    public static void main(String[] args) {
        try {

            DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, 60000);
            DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

            Layer2D.Dimensions inputDimension = new Layer2D.Dimensions(32, 32);
            Kernel convolutionKernel = new Kernel(5, 5);
            Kernel poolingKernel = new Kernel(2, 2);

            ConvolutionalNetwork convolutionNetwork = new ConvolutionalNetwork.Builder(inputDimension, 1)
                    .withConvolutionLayer(convolutionKernel, 10)
                    .withPoolingLayer(poolingKernel)
                    .withConvolutionLayer(convolutionKernel, 1)
                    .withPoolingLayer(poolingKernel)
                    .withConvolutionLayer(convolutionKernel, 1)
                    .withFullConnectedLayer(10)
                    .createNetwork();

            BackPropagation backPropagation = new MomentumBackpropagation();
            backPropagation.setLearningRate(0.0001);
            backPropagation.setMaxError(0.00001);
            backPropagation.setMaxIterations(500);
            backPropagation.addListener(new LearningListener());
            backPropagation.setErrorFunction(new MeanSquaredError());

            convolutionNetwork.setLearningRule(backPropagation);
            backPropagation.addListener(new LearningListener());

            System.out.println("Started training...");
            
            convolutionNetwork.learn(testSet);
                        
            System.out.println("Done training!");
      
            CrossValidation crossValidation = new CrossValidation(convolutionNetwork, testSet, 6);
            crossValidation.run();
            
//           ClassificationMetrics validationResult = crossValidation.computeErrorEstimate(convolutionNetwork, trainSet);
           // Evaluation.runFullEvaluation(convolutionNetwork, testSet);
            
            convolutionNetwork.save("/mnist.nnet");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class LearningListener implements LearningEventListener {


        long start = System.currentTimeMillis();

        @Override
        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            LOG.error("Current iteration: " + bp.getCurrentIteration());
            LOG.error("Error: " + bp.getTotalNetworkError());
            LOG.error("Calculation time: " + (System.currentTimeMillis() - start) / 1000.0);
         //   neuralNetwork.save(bp.getCurrentIteration() + "CNN_MNIST" + bp.getCurrentIteration() + ".nnet");
            start = System.currentTimeMillis();
//            NeuralNetworkEvaluationService.completeEvaluation(neuralNetwork, testSet);
        }

    }    


}
