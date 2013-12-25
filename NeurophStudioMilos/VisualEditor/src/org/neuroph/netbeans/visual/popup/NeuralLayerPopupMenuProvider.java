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
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.dialogs.AddLayerLabelDialog;
import org.neuroph.netbeans.visual.dialogs.AddNeuronDialog;
import org.neuroph.netbeans.visual.dialogs.ChangeTransferFunctionDialog;
import org.neuroph.netbeans.visual.palette.NeuralLayerWidgetStack;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.openide.windows.WindowManager;

/**
 *
 * @author hrza
 */
public class NeuralLayerPopupMenuProvider implements PopupMenuProvider {

    private JPopupMenu popupMenu;
    private JMenuItem removeLayerItem;
    private JMenuItem addNeuronItem;
    private JMenuItem cloneLayerItem;
    private JMenuItem connectToLayer;
    private JMenuItem changeTransferFunction;
    private JMenu removeConnections;
    private JMenuItem removeAllOutputConnections;
    private JMenuItem removeAllInputConnections;
    private JMenuItem setLabel;
    private JMenuItem setAsInputLayer;
    private JMenuItem setAsOutputLayer;
    private NeuralNetworkEditor editor;

    public NeuralLayerPopupMenuProvider() {
        createMenus();
    }

    public final void createMenus() {

        popupMenu = new JPopupMenu();
        addNeuronItem = new JMenuItem("Add Neurons");
        removeLayerItem = new JMenuItem("Remove Layer");
        cloneLayerItem = new JMenuItem("Clone Layer");
        connectToLayer = new JMenuItem("Connect to Layer");
        removeConnections = new JMenu("Remove Connections");
        removeAllInputConnections = new JMenuItem("Remove All Input Connections");
        removeAllOutputConnections = new JMenuItem("Remove All Output Connections");
        setLabel = new JMenuItem("Set label");
        setAsInputLayer = new JMenuItem("Set as input layer");
        setAsOutputLayer = new JMenuItem("Set as output layer");
        removeConnections.add(removeAllInputConnections);
        removeConnections.add(removeAllOutputConnections);

        changeTransferFunction = new JMenuItem("Change transfer function to all neurons");

        popupMenu.add(addNeuronItem);
        popupMenu.add(removeLayerItem);
        popupMenu.add(cloneLayerItem);
        popupMenu.add(removeConnections);
        popupMenu.add(changeTransferFunction);
        popupMenu.add(setLabel);
        popupMenu.add(setAsInputLayer);
        popupMenu.add(setAsOutputLayer);

    }

    @Override
    public JPopupMenu getPopupMenu(final Widget widget, Point point) {
        editor = ((NeuralNetworkScene) widget.getScene()).getNeuralNetworkEditor();
        removeLayerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this layer?", "Delete Layer?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Layer myLayer = ((NeuralLayerWidget) widget).getlookup().lookup(Layer.class);
                    editor.removeLayer(myLayer);
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                }
            }
        });

        addNeuronItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNeuronDialog dialog = new AddNeuronDialog(null, true, ((NeuralLayerWidget) widget).getLayer(), (NeuralNetworkScene) widget.getScene());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                //((NeuralNetworkScene) widget.getScene()).setRefresh(false);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        cloneLayerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clone The Layer X times 
                String anwser = JOptionPane.showInputDialog(null, "Enter number of clones for layer");
                int number = Integer.parseInt(anwser);
                if (number > 0) {
                    Layer newLayer = new Layer();
                    Layer clonningClient = ((NeuralLayerWidget) widget).getLayer();
                    for (Neuron n : clonningClient.getNeurons()) {
                        Neuron newNeuron = new Neuron();
                        editor.setNeuronInputFunction(newNeuron, n.getInputFunction());
                        editor.setNeuronTransferFunction(newNeuron, n.getTransferFunction());
                        //newNeuron.setTransferFunction(n.getTransferFunction());
                        //newNeuron.setInputFunction(newNeuron.getInputFunction());
                        //newLayer.addNeuron(newNeuron);
                        editor.addNeuron(newLayer, newNeuron);
                    }
                    NeuralNetworkScene scene = ((NeuralNetworkScene) widget.getScene());
                    int layerIdx = 0;
                    while (number > 0) {
                        layerIdx = scene.getNeuralNetwork().indexOf(clonningClient) + 1;
                        editor.addEmptyLayer(layerIdx, newLayer);
                        number--;
                    }
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                }
            }
        });

        setLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddLayerLabelDialog dialog = new AddLayerLabelDialog(null, true, ((NeuralLayerWidget) widget).getLayer(), (NeuralNetworkScene) widget.getScene());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        connectToLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Preko NeuralLayerWidget stacka pamtimi selektovani 
                // Layer widget, pa ce trebati i spoljni boolean koji  
                // pokazuje da li je selektovan neki widget...  
                ((NeuralNetworkScene) widget.getScene()).setWaitingLayerClick(true);
                NeuralLayerWidgetStack.setConnectWidget((NeuralLayerWidget) widget);


            }
        });

        changeTransferFunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeTransferFunctionDialog dialog = new ChangeTransferFunctionDialog(null, true, ((NeuralLayerWidget) widget).getLayer(), (NeuralNetworkScene) ((NeuralLayerWidget) widget).getScene());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        removeAllInputConnections.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Layer layer = ((NeuralLayerWidget) widget).getLayer();
                editor.removeAllInputConnections(layer);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        removeAllOutputConnections.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Layer layer = ((NeuralLayerWidget) widget).getLayer();
                editor.removeAllOutputConnections(layer);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });
        
        setAsInputLayer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Layer layer = ((NeuralLayerWidget) widget).getLayer();
                editor.setAsInputLayer(layer);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
            
        });
        
            setAsOutputLayer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Layer layer = ((NeuralLayerWidget) widget).getLayer();
                editor.setAsOutputLayer(layer);
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
            
        });

        return popupMenu;
    }
}
