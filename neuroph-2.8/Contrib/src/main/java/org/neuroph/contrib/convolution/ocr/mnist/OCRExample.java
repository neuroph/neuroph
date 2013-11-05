package org.neuroph.contrib.convolution.ocr.mnist;

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

import org.neuroph.contrib.convolution.ConvolutionLayer;
import org.neuroph.contrib.convolution.ConvolutionNeuralNetwork;
import org.neuroph.contrib.convolution.ConvolutionUtils;
import static org.neuroph.contrib.convolution.ConvolutionUtils.connectFeatureMaps;
import org.neuroph.contrib.convolution.FeatureMapsLayer;
import org.neuroph.contrib.convolution.InputMapsLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.contrib.convolution.PoolingLayer;
import org.neuroph.contrib.convolution.learning.ConvolutionBackpropagation;
import org.neuroph.contrib.convolution.util.ModelMetric;
import org.neuroph.contrib.convolution.util.WeightVisualiser;
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
import org.neuroph.util.LayerFactory;
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

			DataSet trainSet = MNISTLoader.loadTrainSet(1000);
			DataSet testSet = MNISTLoader.loadTestSet(100);

                        LearningListener listener = new LearningListener();
                        
			ConvolutionNeuralNetwork cnn = new ConvolutionNeuralNetwork();

			Layer2D.Dimension mapSize = new Layer2D.Dimension(28, 28);
			Kernel convolutionKernel = new Kernel(5, 5);
			Kernel poolingKernel = new Kernel(2, 2);

			InputMapsLayer inputLayer = new InputMapsLayer(mapSize);
			ConvolutionLayer convolutionLayer1 = new ConvolutionLayer(inputLayer, convolutionKernel);
			PoolingLayer poolingLayer1 = new PoolingLayer(convolutionLayer1, poolingKernel);
			ConvolutionLayer convolutionLayer2 = new ConvolutionLayer(poolingLayer1, convolutionKernel);
			PoolingLayer poolingLayer2 = new PoolingLayer(convolutionLayer2, poolingKernel);
			ConvolutionLayer convolutionLayer3 = new ConvolutionLayer(poolingLayer2, new Kernel(4, 4));

			cnn.addLayer(inputLayer);
			cnn.addLayer(convolutionLayer1);
			cnn.addLayer(poolingLayer1);
			cnn.addLayer(convolutionLayer2);
			cnn.addLayer(poolingLayer2);
			cnn.addLayer(convolutionLayer3);

                        addFeatureMaps(inputLayer, 1);
			addFeatureMaps(convolutionLayer1, 6);
			addFeatureMaps(poolingLayer1, 6);
			addFeatureMaps(convolutionLayer2, 16);
			addFeatureMaps(poolingLayer2, 16);
			addFeatureMaps(convolutionLayer3, 120);

			System.out.println(convolutionLayer1.getDimension());
			System.out.println(poolingLayer1.getDimension());
			System.out.println(convolutionLayer2.getDimension());
			System.out.println(poolingLayer2.getDimension());
			System.out.println(convolutionLayer3.getDimension());

			ConvolutionUtils.fullConectMapLayers(inputLayer, convolutionLayer1);
			ConvolutionUtils.fullConectMapLayers(convolutionLayer1, poolingLayer1);
			ConvolutionUtils.fullConectMapLayers(poolingLayer1, convolutionLayer2);
			ConvolutionUtils.fullConectMapLayers(convolutionLayer2, poolingLayer2);
			ConvolutionUtils.fullConectMapLayers(poolingLayer2, convolutionLayer3);

			NeuronProperties neuronProperties = new NeuronProperties();
			neuronProperties.setProperty("useBias", true);
			neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
			neuronProperties.setProperty("inputFunction", WeightedSum.class);

			Layer outputLayer = new Layer(10, neuronProperties);

                        cnn.addLayer(outputLayer);
			fullConnect(convolutionLayer3, outputLayer, true);

			cnn.setInputNeurons(inputLayer.getNeurons());
			cnn.setOutputNeurons(outputLayer.getNeurons());

			cnn.setLearningRule(new ConvolutionBackpropagation());
			cnn.getLearningRule().setLearningRate(0.05);
			cnn.getLearningRule().setMaxIterations(20);

                        
                        cnn.getLearningRule().addListener(listener);
                        
                        
			long start = System.currentTimeMillis();
			cnn.learn(trainSet);
			System.out.println((System.currentTimeMillis() - start) / 1000.0);
			ModelMetric.calculateModelMetric(cnn, testSet);
			// for (Connection conn :
			// poolingLayer1.getNeurons()[0].getInputConnections())
			// System.out.println(conn.getWeight().getValue());
			// test(cnn, testSet);

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

	// Ovde definitivno ima smisla kreirati metodu getNeuronPrope().
	// Bilo bi dobro kreirati sledeci konstruktor za Layer 2D
	// Layer2D(FeatureMapsLayer topContainer)
	private static void addFeatureMaps(FeatureMapsLayer hiddenLayer, int mapCount) {
		for (int i = 0; i < mapCount; i++) {
			// Layer2D featureMap = new Layer2D(hiddenLayer.getDimension(),
			// hiddenLayer.getNeuronProperties());
			Layer2D featureMap;
			if (hiddenLayer instanceof ConvolutionLayer) {
				featureMap = new Layer2D(hiddenLayer.getDimension(), ConvolutionLayer.DEFAULT_NEURON_PROP);
			} else {
				featureMap = new Layer2D(hiddenLayer.getDimension(), PoolingLayer.DEFAULT_NEURON_PROP);
			}
			hiddenLayer.addFeatureMap(featureMap);
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

	public static void test(ConvolutionNeuralNetwork cnn, DataSet testSet) {
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
