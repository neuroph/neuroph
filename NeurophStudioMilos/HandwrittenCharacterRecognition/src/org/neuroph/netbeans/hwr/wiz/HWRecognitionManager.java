package org.neuroph.netbeans.hwr.wiz;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTextArea;
import org.neuroph.imgrec.image.Image;
import org.neuroph.ocr.OcrPlugin;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.image.ImageJ2SE;

/**
 *
 * @author Vlada-laptop
 */
public class HWRecognitionManager {

    private static HWRecognitionManager instance;

    private NeuralNetwork neuralNetwork;


    public static HWRecognitionManager getInstance() {
        if(instance == null){
            instance = new HWRecognitionManager();
        }
        return instance;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public void recognize(JList probabilitiesList, BufferedImage imageLetter, JTextArea jTextArea1, JCheckBox jCheckBox1) {
        ((DefaultListModel) probabilitiesList.getModel()).clear();

        OcrPlugin ocrPlugin = (OcrPlugin) neuralNetwork.getPlugin(OcrPlugin.class);
        HashMap recognitionResult = ocrPlugin.recognizeCharacterProbabilities(new ImageJ2SE(imageLetter)); // TODO: also provide methid which takes BufferedImage as parameter

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
