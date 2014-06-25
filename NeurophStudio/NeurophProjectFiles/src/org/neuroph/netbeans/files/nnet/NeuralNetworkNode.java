/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.files.nnet;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author zoran
 */
public class NeuralNetworkNode extends DataNode {

    public NeuralNetworkNode(NeuralNetworkDataObject neuralNetworkDataObj, Children ch, Lookup lookup) {
        super(neuralNetworkDataObj, ch, lookup);
        setDisplayName(neuralNetworkDataObj.getNeuralNetwork().getLabel());        
        setShortDescription("Neural network file");        
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }
           
}
