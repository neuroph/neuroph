package org.neuroph.netbeans.main.easyneurons.samples.mlperceptron;

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
 * @author Marko
 */
public class TransferableObject implements Transferable {

    private Object obj = null;
    public static final DataFlavor flavorN = new DataFlavor(NeuralNetwork.class, "NN");
    public static final DataFlavor flavorT = new DataFlavor(DataSet.class, "TS");
    public static final DataFlavor[] flavors = {
        TransferableObject.flavorN,
        TransferableObject.flavorT
    };
    private static final List flavorList = Arrays.asList(flavors);

    // konstruktor, ulazni objekat klasifikuje u odnosu na to kojoj klasi pripada
    public TransferableObject(Object o) {
        if (o instanceof NeuralNetwork) {
            obj = (NeuralNetwork) o;
//            System.out.println("Instance of NeuralNetwork");
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
        if (flavor.equals(TransferableObject.flavorN)) {//||(TransferableObj.flavor_TS.equals(flavor))) {
            return obj;
        } else if (TransferableObject.flavorT.equals(flavor)) {
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
