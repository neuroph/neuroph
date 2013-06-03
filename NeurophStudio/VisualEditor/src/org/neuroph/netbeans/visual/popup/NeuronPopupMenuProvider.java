package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.dialogs.AddNeuronLabelDialog;
import org.neuroph.netbeans.visual.dialogs.ChangeTransferFunctionDialog;
import org.neuroph.netbeans.visual.palette.NeuronWidgetStack;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.openide.windows.WindowManager;

/**
 *
 * @author Damir,Vedrana,Marjan
 */
public class NeuronPopupMenuProvider implements PopupMenuProvider {

    JPopupMenu neuronPopupMenu;
    NeuralNetworkEditor editor;
    NeuralNetwork neuralNet;

    @Override
    public JPopupMenu getPopupMenu(final Widget widget, Point point) {
        editor = ((NeuralNetworkScene) widget.getScene()).getNeuralNetworkEditor();
        neuronPopupMenu = new JPopupMenu();
        Neuron neuron = ((NeuronWidget) widget).getNeuron();
        JMenuItem removeItem = new JMenuItem("Remove Neuron ");
        JMenuItem cloneItem = new JMenuItem("Clone Neuron ");
        JMenuItem createConnection = new JMenuItem("Connect Neuron to.. ");
        JMenu removeConnections = new JMenu("Remove Connections");
        JMenuItem removeAllOutConnections = new JMenuItem("Remove All Output Connections");
        JMenuItem removeAllInputConnections = new JMenuItem("Remove All Input Connections");
        JMenuItem setLabel = new JMenuItem("Set label");
        JMenuItem changeTransferFunction = new JMenuItem("Change Transfer Function");
        JMenuItem changeInputFunction = new JMenuItem("Change Input Function");
        JMenuItem setAsInputNeuron = new JMenuItem("Set as input");
        JMenuItem setAsOutputNeuron = new JMenuItem("Set as output");
        JMenuItem removeFromInput = new JMenuItem("Remove from input neurons");
        JMenuItem removeFromOutput = new JMenuItem("Remove from output neurons");
        removeConnections.add(removeAllInputConnections);
        removeConnections.add(removeAllOutConnections);
        neuronPopupMenu.add(removeItem);
        neuronPopupMenu.add(cloneItem);
      //  neuronPopupMenu.add(createConnection);
        neuronPopupMenu.add(setLabel);
        neuronPopupMenu.add(removeConnections);
        neuronPopupMenu.add(changeTransferFunction);
        neuronPopupMenu.add(changeInputFunction);
        neuronPopupMenu.add(setAsInputNeuron);
        neuronPopupMenu.add(setAsOutputNeuron);
        neuronPopupMenu.add(removeFromInput);
        neuronPopupMenu.add(removeFromOutput);
        if (editor.isInputNeuron(neuron)) {
            setAsInputNeuron.setEnabled(false);
            removeFromInput.setEnabled(true);
        }else {
            removeFromInput.setEnabled(false);
        }
        if (editor.isOutputNeuron(neuron)) {
            setAsOutputNeuron.setEnabled(false);
            removeFromOutput.setEnabled(true);
        }else{
            removeFromOutput.setEnabled(false);
        }

        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this neuron?", "Delete Neuron?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Neuron neuron = ((NeuronWidget) widget).getNeuron();
                    editor.removeNeuron(neuron);
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                }

            }
        });


        cloneItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NeuronWidget parent = (NeuronWidget) widget;
                Neuron neuron = parent.getNeuron();
                String anwser = JOptionPane.showInputDialog("Enter number of clones you want to add");
                int number = Integer.parseInt(anwser);
                Layer layer = neuron.getParentLayer();
                Class< ? extends Neuron> cloningClient = neuron.getClass();
                while (--number >= 0) {
                    try {
                        Neuron newNeuron = cloningClient.newInstance();
                        editor.setNeuronTransferFunction(newNeuron, neuron.getTransferFunction());
                        editor.setNeuronInputFunction(newNeuron, neuron.getInputFunction());
                        editor.addNeuron(layer, newNeuron);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }

                ((NeuralNetworkScene) parent.getScene()).refresh();
            }
        });

        createConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
                scene.setWaitingClick(true);
                NeuronWidget neuronWidget = (NeuronWidget) widget;
                NeuronWidgetStack.connectionneuron = neuronWidget;
            }
        });

        removeAllInputConnections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Neuron neuron = ((NeuronWidget) widget).getNeuron();
                editor.removeAllInputConnections(neuron);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        removeAllOutConnections.addActionListener(new ActionListener() {    // ?Proveriti da li je output widget, mora drugacije
            public void actionPerformed(ActionEvent e) {
                Neuron neuron = ((NeuronWidget) widget).getNeuron();
                editor.removeAllOutputConnections(neuron);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        setLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddNeuronLabelDialog dialog = new AddNeuronLabelDialog(null, true, ((NeuronWidget) widget).getNeuron(), (NeuralNetworkScene) widget.getScene());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });
        changeTransferFunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NeuronWidget parent = (NeuronWidget) widget;
                Neuron neuron = parent.getNeuron();

                ChangeTransferFunctionDialog dialog = new ChangeTransferFunctionDialog(null, true, neuron, (NeuralNetworkScene) widget.getScene());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });
        setAsInputNeuron.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editor.setAsInputNeuron(((NeuronWidget) widget).getNeuron());
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        setAsOutputNeuron.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editor.setAsOutputNeuron(((NeuronWidget) widget).getNeuron());
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        changeInputFunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NeuronWidget parent = (NeuronWidget) widget;
                Neuron neuron = parent.getNeuron();

                //  ChangeTransferFunctionDialog dialog= new ChangeTransferFunctionDialog(null,true,neuron); 
                //   dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow()); 
                //   dialog.setVisible(true);
                // ((NeuralNetworkScene) widget.getScene()).refresh(); 
            }
        });

        removeFromInput.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
               editor.removeFromInputNeurons(((NeuronWidget)widget).getNeuron());
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
            
        });

         removeFromOutput.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
               editor.removeFromOutputNeurons(((NeuronWidget)widget).getNeuron());
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
            
        });
        return neuronPopupMenu;
    }

    
}
