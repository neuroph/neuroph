package org.neuroph.training;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zoran
 */
public class StoreTrainingResultTask extends Task {

    // store this field as process var
    List<TrainingResult> trainingResults = null;    
        
    public StoreTrainingResultTask(String name) {
        super(name);
        
    }
    
    @Override
    public void execute() {
        if (trainingResults == null) {
                trainingResults = new ArrayList<>();
                getParentProcess().setVar("trainingResults", trainingResults);    
        }
        
        TrainingResult trainingResult = (TrainingResult) getParentProcess().getVar("trainingResult");
        trainingResults.add(trainingResult);  
    }

    public List<TrainingResult> getTrainingResults() {
        return trainingResults;
    }
                
}
