package org.neuroph.samples.convolution;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.ConvolutionalUtils;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.nnet.learning.ConvolutionalBackpropagation;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Konvolucioni parametri
 * 
 * Globalna arhitektura: Konvolucioni i pooling lejeri - naizmenicno (samo konvolucioni ili naizmenicno konvolccioni pooling)
 * Za svaki lajer da ima svoj kernel (mogu svi konvolucioni da imaju isti kernel, ili svi pooling isti kernel)
 * da mogu da zadaju neuron properties (transfer funkciju) za konvolucioni i pooling lejer(input)
 * Konektovanje lejera? - po defaultu full connect (ostaviti api za custom konekcije)
 * 
 * addFeatureMaps...
 * connectFeatureMaps
 * 
 * Helper utility klasa...
 * 
 * Osnovni kriterijumi:
 * 1. Jednostavno kreiranje default neuronske mreze
 * 2. Laka customizacija i kreiranje custom arhitektura: konvolucionih i pooling lejera i transfer/input funkcija
 * Napraviti prvo API i ond aprilagodti kod
 * 
 * ------------------------
 * 
 * promeniti nacin kreiranja i dodavanja feature maps layera
 * resiti InputMaps Layer, overridovana metoda koja baca unsupported exception ukazuje da nesto nije u redu sa dizajnom
 * Da li imamo potrebe za klasom kernel  - to je isto kao i dimension?
 * 
 * zasto je public abstract void connectMaps apstraktna? (u klasi FeatureMapsLayer)
 * 
 * InputMapsLayer konstruktoru superklase prosledjuje null...
 * 
 * fullConectMapLayers
 * 
 * @author zoran
 */


public class OCRExample {
    
        static class LearningListener implements LearningEventListener {

            public void handleLearningEvent(LearningEvent event) {
                BackPropagation bp = (BackPropagation)event.getSource();
                System.out.println("Current iteration: "+bp.getCurrentIteration());
                System.out.println("Error: "+bp.getTotalNetworkError());
            }
            
        }
    

	public static void main(String[] args) {
		try {

			// create training and test set from files
                        DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, 
                                                                       MNISTDataSet.TRAIN_IMAGE_NAME,
                                                                       1000);
			
                        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, 
                                                                      MNISTDataSet.TEST_IMAGE_NAME,
                                                                      100);


                        // create convolutional neural network                                                                       
			ConvolutionalNetwork convolutionalNet = new ConvolutionalNetwork();

			Layer2D.Dimensions inputMapSize = new Layer2D.Dimensions(28, 28);
			Kernel convolutionKernel = new Kernel(5, 5);
			Kernel poolingKernel = new Kernel(2, 2);

			InputMapsLayer inputLayer = new InputMapsLayer(inputMapSize, 1);                                               
                        // just add number of maps to this constructor, and provide constructors with neuronProperties
			ConvolutionalLayer convolutionLayer1 = new ConvolutionalLayer(inputLayer, convolutionKernel, 6);
			PoolingLayer poolingLayer1 = new PoolingLayer(convolutionLayer1, poolingKernel);
			ConvolutionalLayer convolutionLayer2 = new ConvolutionalLayer(poolingLayer1, convolutionKernel, 16);
			PoolingLayer poolingLayer2 = new PoolingLayer(convolutionLayer2, poolingKernel);
			ConvolutionalLayer convolutionLayer3 = new ConvolutionalLayer(poolingLayer2, new Kernel(4, 4), 120);
                                        
			convolutionalNet.addLayer(inputLayer);
			convolutionalNet.addLayer(convolutionLayer1);
			convolutionalNet.addLayer(poolingLayer1);
			convolutionalNet.addLayer(convolutionLayer2);
			convolutionalNet.addLayer(poolingLayer2);
			convolutionalNet.addLayer(convolutionLayer3);
                       
			ConvolutionalUtils.fullConectMapLayers(inputLayer, convolutionLayer1);
			ConvolutionalUtils.fullConectMapLayers(convolutionLayer1, poolingLayer1);
			ConvolutionalUtils.fullConectMapLayers(poolingLayer1, convolutionLayer2);
			ConvolutionalUtils.fullConectMapLayers(convolutionLayer2, poolingLayer2);
			ConvolutionalUtils.fullConectMapLayers(poolingLayer2, convolutionLayer3);

			NeuronProperties neuronProperties = new NeuronProperties();
			neuronProperties.setProperty("useBias", true);
			neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
			neuronProperties.setProperty("inputFunction", WeightedSum.class);

			Layer outputLayer = new Layer(10, neuronProperties);

                        convolutionalNet.addLayer(outputLayer);
			fullConnect(convolutionLayer3, outputLayer, true);

                        // this should be set by default
			convolutionalNet.setInputNeurons(inputLayer.getNeurons());
			convolutionalNet.setOutputNeurons(outputLayer.getNeurons());

			convolutionalNet.setLearningRule(new ConvolutionalBackpropagation());
			convolutionalNet.getLearningRule().setLearningRate(0.05);
			convolutionalNet.getLearningRule().setMaxIterations(20);
                        
                        
                        //----------------------------------------------------------------------
                        
			System.out.println(convolutionLayer1.getMapDimensions());
			System.out.println(poolingLayer1.getMapDimensions());
			System.out.println(convolutionLayer2.getMapDimensions());
			System.out.println(poolingLayer2.getMapDimensions());
			System.out.println(convolutionLayer3.getMapDimensions());                        
                        
                        
                        // create and set learning listener 
                        LearningListener listener = new LearningListener();                        
                        convolutionalNet.getLearningRule().addListener(listener);
                        
			long start = System.currentTimeMillis();
			convolutionalNet.learn(trainSet);
			System.out.println((System.currentTimeMillis() - start) / 1000.0);
			ModelMetric.calculateModelMetric(convolutionalNet, testSet);


			WeightVisualiser visualiser1 = new WeightVisualiser(convolutionLayer1.getFeatureMap(0), convolutionKernel);
			visualiser1.displayWeights();

			WeightVisualiser visualiser2 = new WeightVisualiser(convolutionLayer1.getFeatureMap(1), convolutionKernel);
			visualiser2.displayWeights();

			WeightVisualiser visualiser3 = new WeightVisualiser(convolutionLayer1.getFeatureMap(2), convolutionKernel);
			visualiser3.displayWeights();

			WeightVisualiser visualiser4 = new WeightVisualiser(convolutionLayer1.getFeatureMap(3), convolutionKernel);
			visualiser4.displayWeights();

			WeightVisualiser visualiser5 = new WeightVisualiser(convolutionLayer1.getFeatureMap(4), convolutionKernel);
			visualiser5.displayWeights();

			WeightVisualiser visualiser6 = new WeightVisualiser(convolutionLayer1.getFeatureMap(5), convolutionKernel);
			visualiser6.displayWeights();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

 
	public static void saveImage(Layer2D outMap) throws IOException {
		BufferedImage finalImage = new BufferedImage(outMap.getWidth(), outMap.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		int[] rgbData = new int[outMap.getWidth() * outMap.getHeight()];
		for (int y = 0; y < outMap.getHeight(); y++) {
			for (int x = 0; x < outMap.getWidth(); x++) {
				int val = (int) (outMap.getNeuronAt(x, y).getOutput() * 255);
				rgbData[y * (outMap.getWidth()) + x] = new Color(val, val, val).getRGB();
			}
		}

		finalImage.setRGB(0, 0, outMap.getWidth(), outMap.getHeight(), rgbData, 0, outMap.getWidth());
		File f = new File("E:\\Coursera\\Images\\CNN" + (int) (Math.random() * 1000) + ".bmp");
		ImageIO.write(finalImage, "bmp", f);

	}

	public static void test(ConvolutionalNetwork cnn, DataSet testSet) {
		double sum = 0;
		for (DataSetRow testSetRow : testSet.getRows()) {
			cnn.setInput(testSetRow.getInput());
			cnn.calculate();

			double[] networkOutput = cnn.getOutput();

			System.out.println("Desired: " + Arrays.toString(testSetRow.getDesiredOutput()));
			System.out.println("Actual : " + Arrays.toString(networkOutput));

			for (int i = 0; i < networkOutput.length; i++) {
				sum += Math.pow(testSetRow.getDesiredOutput()[i] - networkOutput[i], 2) * 0.5;
			}
		}
		System.out.println(sum / testSet.getRows().size());

	}

	public static BufferedImage getFlippedImage(BufferedImage bi) {
		BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
		AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
		tran.concatenate(flip);

		Graphics2D g = flipped.createGraphics();
		g.setTransform(tran);
		g.drawImage(bi, 0, 0, null);
		g.dispose();

		return flipped;
	}

	public static byte[] buff2byte(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		byte[] imageBytes = data.getData();
		return imageBytes;
	}

	public static void fullConnect(FeatureMapsLayer fromLayer, Layer toLayer, boolean connectBiasNeuron) {
		for (Neuron fromNeuron : fromLayer.getNeurons()) {
			if (fromNeuron instanceof BiasNeuron) {
				continue;
			}
			for (Neuron toNeuron : toLayer.getNeurons()) {
				ConnectionFactory.createConnection(fromNeuron, toNeuron);
			}
		}
	}
}
