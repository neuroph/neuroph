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
import org.neuroph.contrib.convolution.FeatureMapsLayer;
import org.neuroph.contrib.convolution.InputMapsLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.contrib.convolution.PoolingLayer;
import org.neuroph.contrib.convolution.util.ModelMetric;
import org.neuroph.contrib.convolution.util.WeightVisualiser;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class OCRExample {

    public static void main(String[] args) {
        try {

            String path = "/home/zoran/Desktop/Convolutional";

            DataSet trainSet = MNISTLoader.loadTrainSet(path, 2000);
            DataSet testSet = MNISTLoader.loadTestSet(path, 10000);

            ConvolutionNeuralNetwork cnn = new ConvolutionNeuralNetwork();

            Layer2D.Dimension mapSize = new Layer2D.Dimension(28, 28);
            Kernel convolutionKernel = new Kernel(5, 5);
            Kernel endConvolutionKernel = new Kernel(4, 4);
            Kernel poolingKernel = new Kernel(2, 2);

            InputMapsLayer inputLayer = new InputMapsLayer(mapSize);
            ConvolutionLayer convolutionLayer = new ConvolutionLayer(inputLayer, convolutionKernel);
            FeatureMapsLayer convolutionLayer2 = new ConvolutionLayer(convolutionLayer, convolutionKernel);
            // FeatureMapsLayer convolutionLayer3 = new
            // ConvolutionLayer(convolutionLayer2, convolutionKernel);
            // FeatureMapsLayer convolutionLayer4 = new
            // ConvolutionLayer(convolutionLayer3, convolutionKernel);
            // FeatureMapsLayer convolutionLayer5 = new
            // ConvolutionLayer(convolutionLayer4, convolutionKernel);

            cnn.addLayer(inputLayer);
            cnn.addLayer(convolutionLayer);
            cnn.addLayer(convolutionLayer2);
            // cnn.addLayer(convolutionLayer3);
            // cnn.addLayer(convolutionLayer4);
            // cnn.addLayer(convolutionLayer5);

            addFeatureMaps(convolutionLayer, 6);
            addFeatureMaps(convolutionLayer2, 6);
            // addFeatureMaps(convolutionLayer3, 10);
            // addFeatureMaps(convolutionLayer4, 10);
            // addFeatureMaps(convolutionLayer5, 50);
            System.out.println(convolutionLayer.getDimension());
            System.out.println(convolutionLayer2.getDimension());

            ConvolutionUtils.fullConectMapLayers(inputLayer, convolutionLayer);
            ConvolutionUtils.fullConectMapLayers(convolutionLayer, convolutionLayer2);
            // ConvolutionUtils.fullConectMapLayers(convolutionLayer2,
            // convolutionLayer3);
            // ConvolutionUtils.fullConectMapLayers(convolutionLayer3,
            // convolutionLayer4);
            // ConvolutionUtils.fullConectMapLayers(convolutionLayer4,
            // convolutionLayer5);

            NeuronProperties neuronProperties = new NeuronProperties();
            neuronProperties.setProperty("useBias", true);
            neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
            neuronProperties.setProperty("inputFunction", WeightedSum.class);

            Layer outputLayer = LayerFactory.createLayer(10, neuronProperties); // this
            // this
            // shpuld
            // be
            // map
            // layer?
            cnn.addLayer(outputLayer);
            fullConnect(convolutionLayer2, outputLayer, true);

            cnn.setInputNeurons(inputLayer.getNeurons());
            cnn.setOutputNeurons(outputLayer.getNeurons());

            cnn.getLearningRule().setLearningRate(0.2);
            cnn.getLearningRule().setMaxIterations(8);

            cnn.learn(trainSet);
            ModelMetric.calculateModelMetric(cnn, testSet);

            WeightVisualiser visualiser1 = new WeightVisualiser(convolutionLayer.getFeatureMap(0), convolutionKernel);
            visualiser1.displayWeights();

            WeightVisualiser visualiser2 = new WeightVisualiser(convolutionLayer.getFeatureMap(1), convolutionKernel);
            visualiser2.displayWeights();

            WeightVisualiser visualiser3 = new WeightVisualiser(convolutionLayer.getFeatureMap(2), convolutionKernel);
            visualiser3.displayWeights();
            WeightVisualiser visualiser4 = new WeightVisualiser(convolutionLayer.getFeatureMap(3), convolutionKernel);
            visualiser4.displayWeights();
            WeightVisualiser visualiser5 = new WeightVisualiser(convolutionLayer.getFeatureMap(4), convolutionKernel);
            visualiser5.displayWeights();
            WeightVisualiser visualiser6 = new WeightVisualiser(convolutionLayer.getFeatureMap(5), convolutionKernel);
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
                featureMap = new Layer2D(hiddenLayer.getDimension(), ConvolutionLayer.neuronProperties);
            } else {
                featureMap = new Layer2D(hiddenLayer.getDimension(), PoolingLayer.neuronProperties);
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
