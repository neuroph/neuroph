package org.neuroph.netbeans.visual.dialogs;

import javax.swing.JOptionPane;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.core.transfer.TransferFunction;

/*
 * Where is this used, and why?
 * @author hrza
 */
public class NeuralFactory {

    public static Neuron createNeuronInstance(String className) {
        Neuron neuron;
        try {
            Class<? extends Neuron> someClass = (Class<? extends Neuron>) Class.forName(className);
            neuron = (Neuron) someClass.newInstance();
            return neuron;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error! Default instance was returned");
            neuron = new Neuron();
        }
        return neuron;

    }

    public static Layer createLayerInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Layer layer;
        try {
            Class<? extends Layer> someLayerClass = (Class<? extends Layer>) Class.forName(className);
            layer = (Layer) someLayerClass.newInstance();
            //return layer; 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error! Default instance was returned");
            layer = new Layer();
        }
        return layer;
    }

    public static TransferFunction createTransferFunctionInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        TransferFunction transferFunction;
        try {
            Class<? extends TransferFunction> someTFClass = (Class<? extends TransferFunction>) Class.forName(className);
            transferFunction = (TransferFunction) someTFClass.newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error! Default instance was returned");
            transferFunction = new Gaussian();
        }
        return transferFunction;
    }

    public static InputFunction createInputFunctionInstance(String className) {
        InputFunction inputFunction;
        try {
            Class<? extends InputFunction> someIFclass = (Class<? extends InputFunction>) Class.forName(className);
            inputFunction = (InputFunction) someIFclass.newInstance();
        } catch (Exception e) {
            inputFunction = null;
        }
        return inputFunction;
    }
}
