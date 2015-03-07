package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.palette.PaletteItemNode;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author zoran
 * @author Boris Perović
 */
public class NeuronWidgetAcceptProvider implements AcceptProvider {

    private final NeuronWidget neuronWidget;
    Graphics2D graphics;
    NeuralNetworkEditor editor;

    public NeuronWidgetAcceptProvider(NeuronWidget neuronWidget) {
        this.neuronWidget = neuronWidget;
        this.editor = ((NeuralNetworkScene) neuronWidget.getScene()).getNeuralNetworkEditor();
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        if (node instanceof PaletteItemNode) {
            PaletteItemNode pin = (PaletteItemNode) node;
            Image dragImage = ImageUtilities.loadImage(pin.getPaletteItem().getIcon());
            JComponent view = widget.getScene().getView();
            Graphics2D g2 = (Graphics2D) view.getGraphics();
            view.paintImmediately(view.getVisibleRect());

            Point globalPoint = widget.convertLocalToScene(point);
            g2.drawImage(dragImage, globalPoint.x, globalPoint.y, view);
            
            Class droppedClass = pin.getPaletteItem().getDropClass();
            return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
        } else {
            return ConnectorState.REJECT;
        }
    }
    
    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        PaletteItemNode pin = (PaletteItemNode) node;
        Class droppedClass = pin.getPaletteItem().getDropClass();
        try {
            if (droppedClass.getSuperclass().equals(TransferFunction.class)) {
                TransferFunction transferFunction = (TransferFunction) droppedClass.newInstance();
                editor.setNeuronTransferFunction(neuronWidget.getNeuron(), transferFunction);
            } else if (droppedClass.getSuperclass().equals(InputFunction.class)) {
                InputFunction inputFunction = (InputFunction) droppedClass.newInstance();
                editor.setNeuronInputFunction(neuronWidget.getNeuron(), inputFunction);
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean canAccept(Class droppedClass) {
        if (droppedClass.getSuperclass().equals(TransferFunction.class)
                || droppedClass.equals(InputFunction.class)) {
            return true;
        } else {
            return false;
        }
    }
}
