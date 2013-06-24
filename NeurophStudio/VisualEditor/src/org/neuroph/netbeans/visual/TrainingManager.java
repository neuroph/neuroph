package org.neuroph.netbeans.visual;

import org.neuroph.netbeans.visual.NeuralNetworkTraining;
import java.util.Collection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author zoran
 */
public class TrainingManager implements LookupListener {
    
private static TrainingManager instance=null;

    private NeuralNetwork nnet;
    private DataSet dataSet;
//    Lookup.Result<NeuralNetwork> nnResult;
    Lookup.Result<DataSet> dsResult;
    
    NeuralNetworkTraining training;
    
    
    
    private TrainingManager() {
        Lookup global = Utilities.actionsGlobalContext();
//        nnResult = global.lookupResult(NeuralNetwork.class);
//        nnResult.addLookupListener(this);
        dsResult = global.lookupResult(DataSet.class);
        dsResult.addLookupListener(this);
    }
    
    public static TrainingManager getDefault() {
        if (instance==null) {
            instance = new TrainingManager();
        }
        
        return instance;
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result results = (Lookup.Result)ev.getSource();
        
        Collection coll = results.allInstances();
        if (!coll.isEmpty()) {
            Object selected = coll.iterator().next();
            
//            if (selected instanceof NeuralNetwork) {
//                nnet = (NeuralNetwork) selected;
//            }
        // if training set is not selected set it to null, and the train button in tooolbar will be disabled
        if (selected instanceof DataSet) {
                dataSet = (DataSet) selected;
            }             
        } else {
            dataSet = null;
        }
        
    }

    public NeuralNetwork getNeuralNetwork() {
        return nnet;
    }

    public void setNeuralNetwork(NeuralNetwork nnet) {
        this.nnet = nnet;
    }
    
    
    
    public NeuralNetworkTraining getTraining() {
        if ((nnet!=null) && (dataSet!=null)) {
            return new NeuralNetworkTraining(nnet, dataSet);
        }
        return null;
    }
    
    
    
    
    
}
