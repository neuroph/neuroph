package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.dialogs.AddCompetitiveNeuronDialog;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.nnet.comp.neuron.ThresholdNeuron;
import org.openide.windows.WindowManager;

/**
 *
 * @author zoran
 */
public class NeuralLayerWidgetAcceptProvider implements AcceptProvider {
    
    private NeuralLayerWidget layerWidget;
    private NeuralNetworkEditor editor;
    
    public NeuralLayerWidgetAcceptProvider(NeuralLayerWidget layerWidget) {
        this.layerWidget = layerWidget;
        editor = ((NeuralNetworkScene) layerWidget.getScene()).getNeuralNetworkEditor();
    }
    
    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        DataFlavor flavor = t.getTransferDataFlavors()[4];
        Class droppedClass = flavor.getRepresentationClass();
        return isAcceptableWidget(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }
    
    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        
        DataFlavor flavor = t.getTransferDataFlavors()[4];
        Class droppedClass = flavor.getRepresentationClass();
        
        try {
            if (droppedClass.equals(Neuron.class) || droppedClass.getSuperclass().equals(Neuron.class) || droppedClass.equals(CompetitiveNeuron.class) || droppedClass.equals(ThresholdNeuron.class)) {
                onNeuronDroped(droppedClass, point);
            } else if (droppedClass.getClass().equals(TransferFunction.class) || droppedClass.getSuperclass().equals(TransferFunction.class)) {
                // transfer funkcija 
                setTransferFunction(droppedClass);
            } else if (droppedClass.getSuperclass().equals(InputFunction.class)) {
                // input funkcija 
                setInputFunction(droppedClass);
            }
        } catch (Exception e) {
        }
    }
    
    public boolean isAcceptableWidget(Class droppedClass) {
        return (droppedClass.equals(Neuron.class)
                || droppedClass.getSuperclass().equals(Neuron.class)
                || droppedClass.equals(CompetitiveNeuron.class)
                || droppedClass.equals(ThresholdNeuron.class)
                || droppedClass.getSuperclass().equals(TransferFunction.class)
                || droppedClass.getSuperclass().equals(InputFunction.class));
        
    }
    
    public void onNeuronDroped(Class droppedClass, Point point)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Layer layer = layerWidget.getLayer();
        Neuron neuron;
        //TODO uradjeno ubacivanje Competitive Neurona na competitive layer. Nisam znao koji su default inputFunction i transferFunction za njega, pa sam stavio ova dva.
        //sve isto vazi i za Threshold Neuron
        if (droppedClass.equals(CompetitiveNeuron.class)) {
            Class[] parameterTypes = {InputFunction.class, TransferFunction.class};
            CompetitiveNeuron competitiveNeuron = (CompetitiveNeuron) droppedClass.getDeclaredConstructor(parameterTypes).newInstance(new Object[]{new WeightedSum(), new Tanh()});
            
            neuron = (Neuron) competitiveNeuron;
        } else if (droppedClass.equals(ThresholdNeuron.class)) {
            Class[] parameterTypes = {InputFunction.class, TransferFunction.class};
            ThresholdNeuron thresholdNeuron = (ThresholdNeuron) droppedClass.getDeclaredConstructor(parameterTypes).newInstance(new Object[]{new WeightedSum(), new Step()});
            neuron = (Neuron) thresholdNeuron;
        } else {
            neuron = (Neuron) droppedClass.newInstance();
        }
        
        
        if (neuron instanceof InputNeuron && !(layerWidget.getLayer() instanceof InputLayer)) // nothing 
        {
            JOptionPane.showMessageDialog(null, "Input Neurons can only be placed in input layer!");
        } else if (neuron instanceof CompetitiveNeuron && !(layerWidget.getLayer() instanceof CompetitiveLayer)) {
            JOptionPane.showMessageDialog(null, "Competitive Neurons can only be placed in competitive layer!");
        } else if (neuron instanceof CompetitiveNeuron && (layerWidget.getLayer() instanceof CompetitiveLayer)) {
            AddCompetitiveNeuronDialog dialog = new AddCompetitiveNeuronDialog(null, true, layer, (NeuralNetworkScene) layerWidget.getScene());
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setVisible(true);
            ((NeuralNetworkScene) this.layerWidget.getScene()).refresh();
        } else {
            int nonNeuronWidgets = 0;
            int dropIdx = layer.getNeuronsCount();
            for (int i = 0; i < layerWidget.getChildren().size(); i++) { // vraca i neki image widget, i jos jedan, tek treci je NeuronWidget - do je zbog labela koji ispisuju output!!!
                Widget widget2 = layerWidget.getChildren().get(i);
                if (!(widget2 instanceof IconNodeWidget)) { //NeuronWidget
                    nonNeuronWidgets++;
                    continue;
                }
                double neuronWidgetPosition = widget2.getLocation().getX();
                if (point.getX() < neuronWidgetPosition) {
                    dropIdx = i - nonNeuronWidgets;
                    break;
                }
            }
            if (dropIdx == layer.getNeuronsCount()) {
                editor.addNeuron(layer, neuron);
            } else {
                editor.addNeuronAt(layer, neuron, dropIdx);
            }
        }
        ((NeuralNetworkScene) this.layerWidget.getScene()).refresh();
    }
    
    public void setTransferFunction(Class droppedClass) throws InstantiationException, IllegalAccessException, Exception {
        Class<? extends TransferFunction> transferFunction = droppedClass;
        Layer myLayer = layerWidget.getLayer();
        editor.setLayerTransferFunction(myLayer, transferFunction);
    }
    
    public void setInputFunction(Class droppedClass) throws InstantiationException, IllegalAccessException, Exception {
        Class<? extends InputFunction> inputFunction = droppedClass;
        Layer layer = layerWidget.getLayer();
        editor.setLayerInputFunction(layer, inputFunction);
    }
}
