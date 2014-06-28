package org.neuroph.netbeans.hwr.wiz;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.netbeans.imr.utils.ImagesLoader;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.ocr.OcrHelper;
import org.neuroph.ocr.OcrPlugin;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;

/**
 *
 * @author Vlada-laptop
 */
public class HWRUtil {

    private List<String> imageLabels = new ArrayList<String>();
    private NeuralNetwork activeNeuralNetwork;

    public void createTrainigSet(int width, int height) {

        HashMap<String, FractionRgbData> rgbDataMap = new HashMap<String, FractionRgbData>();
        String imageDir = TreeManager.getPath() + "/"; // get path to directory with letter images
                //"Letters/Training Set/";
        try {
            File labeledImagesDir = new File(imageDir);
            rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(labeledImagesDir,
                    new Dimension(width, height)));

            for (String imgName : rgbDataMap.keySet()) {
                StringTokenizer st = new StringTokenizer(imgName, "._");
                String imageLabel = st.nextToken();
                if (!imageLabels.contains(imageLabel)) {
                    imageLabels.add(imageLabel);
                }
            }
            Collections.sort(imageLabels);
        } catch (IOException ioe) {
            System.err.println("Unable to load images from labeled images dir: '" + imageDir + "'");
            System.err.println(ioe.toString());
        }
        DataSet activeTrainingSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(imageLabels, rgbDataMap);
        activeTrainingSet.setLabel("NewLettersTrainingSet");
        
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(activeTrainingSet);
    }

    public void createNeuralNetwork(String comboItem, String neuralNetworkName, String hiddenLayerNeurons) {
        TransferFunctionType transferFunctionType = TransferFunctionType.valueOf(comboItem.toString().toUpperCase());
        String name = neuralNetworkName;
        String hiddenLayersStr = hiddenLayerNeurons;

        ArrayList<Integer> hiddenLayersNeuronsCount;

        try {
            hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr);
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null,
                    "Invalid input! Hidden Layers Neuron Counts must be entered as space separated integers.",
                    "Error", JOptionPane.DEFAULT_OPTION);
            return;
        }

        // create neural network
         activeNeuralNetwork = OcrHelper.createNewNeuralNetwork(name, new Dimension(DrawingPanel.FIXED_WIDTH, DrawingPanel.FIXED_HEIGHT), ColorMode.BLACK_AND_WHITE, imageLabels, hiddenLayersNeuronsCount, transferFunctionType);
         NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(activeNeuralNetwork);
         
//        HWRecognitionManager.getInstance().setNeuralNetwork(activeNeuralNetwork);

    }

    public void recognize(JList probabilitiesList, Image imageLetter, JTextArea jTextArea1, JCheckBox jCheckBox1) {
        ((DefaultListModel) probabilitiesList.getModel()).clear();

        OcrPlugin ocrPlugin = (OcrPlugin) activeNeuralNetwork.getPlugin(OcrPlugin.class);
        HashMap recognitionResult = ocrPlugin.recognizeCharacterProbabilities(imageLetter);

        String[] outputResult = formatOutput(recognitionResult.toString());
        DefaultListModel model = (DefaultListModel) probabilitiesList.getModel();

        for (int i = 0; i < outputResult.length; i++) {
            model.addElement(outputResult[i]);
        }

        probabilitiesList.setSelectedIndex(0);
        if (jCheckBox1.isSelected()) {
            String letter = model.getElementAt(0).toString().substring(0, 1);
            model.clear();
            jTextArea1.append(letter);
            //drawingPanelRecognition.clearDrawingArea();
        }
    }

    private String[] formatOutput(String output) {
        return output.substring(1, output.length() - 1).split(", ");
    }
}
