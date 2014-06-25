package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author zoran
 */
public class NeuronWidgetAcceptProvider implements AcceptProvider {

    // proba 
    private NeuronWidget neuronWidget;
    Graphics2D graphics;
    NeuralNetworkEditor editor;

    public NeuronWidgetAcceptProvider(NeuronWidget neuronWidget) {
        this.neuronWidget = neuronWidget;
        this.editor = ((NeuralNetworkScene)neuronWidget.getScene()).getNeuralNetworkEditor();
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {

//        JOptionPane.showMessageDialog(null, "hello");

        DataFlavor flavor = t.getTransferDataFlavors()[4];
        Class droppedClass = flavor.getRepresentationClass();
        JComponent view = widget.getScene().getView();
        graphics = (Graphics2D) view.getGraphics();

        Rectangle visRect = view.getVisibleRect();
        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);

//        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
//        view.print(graphics);
//        Image dragImage = (Image) t.getTransferData(DataFlavor.imageFlavor);
//        Graphics2D g2 = (Graphics2D) view.getGraphics();
        Image dragImage = getImageFromTransferable(t);
        graphics.drawImage(dragImage,
                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                point.getLocation().getY()),
                null);
//      }
//    });                
        if (droppedClass.getSuperclass().equals(TransferFunction.class)
                || droppedClass.equals(InputFunction.class)) {
            return ConnectorState.ACCEPT;
        } else {
            return ConnectorState.REJECT;
        }
    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException | UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        }
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {

        DataFlavor flavor = t.getTransferDataFlavors()[4];
        Class droppedClass = flavor.getRepresentationClass();
        try {
            if (droppedClass.getSuperclass().equals(TransferFunction.class)) {
                TransferFunction transferFunction = (TransferFunction) droppedClass.newInstance();
                editor.setNeuronTransferFunction(neuronWidget.getNeuron(), transferFunction);
            } else if (droppedClass.getSuperclass().equals(InputFunction.class)) {
                InputFunction inputFunction = (InputFunction) droppedClass.newInstance();
                editor.setNeuronInputFunction(neuronWidget.getNeuron(), inputFunction);
            }
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } 
    }
}
