package org.neuroph.netbeans.classificationsample;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author Marko Koprivica
 * @author Zoran Sevarac
 */
public class NeurophTransferableObject implements Transferable {

    private Object obj = null;
    public static final DataFlavor neuralNetFlavor = new DataFlavor(NeuralNetwork.class, "NN");
    public static final DataFlavor dataSetFlavor = new DataFlavor(DataSet.class, "TS");
    
    public static final DataFlavor[] flavors = {
        NeurophTransferableObject.neuralNetFlavor,
        NeurophTransferableObject.dataSetFlavor
    };
    private static final List flavorList = Arrays.asList(flavors);

    // konstruktor, ulazni objekat klasifikuje u odnosu na to kojoj klasi pripada
    public NeurophTransferableObject(Object o) {
        if (o instanceof NeuralNetwork) {
            obj = (NeuralNetwork) o;
        }

        if (o instanceof DataSet) {
            obj = (DataSet) o;
        }
    }

    // geter za objekat koji se prenosi (nista spec)
    public Object getObj() {
        return obj;
    }

    // Transferable implemented methods:
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(NeurophTransferableObject.neuralNetFlavor)) {//||(TransferableObj.flavor_TS.equals(flavor))) {
            return obj;
        } else if (NeurophTransferableObject.dataSetFlavor.equals(flavor)) {
            return obj;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }

    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return (flavorList.contains(flavor));
    }
}
