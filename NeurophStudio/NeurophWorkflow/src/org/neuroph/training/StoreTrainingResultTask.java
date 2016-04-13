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
                setVariable("trainingResults", trainingResults);    
        }
        
        TrainingResult trainingResult = getVariable("trainingResult", TrainingResult.class);
        trainingResults.add(trainingResult);  
    }

    public List<TrainingResult> getTrainingResults() {
        return trainingResults;
    }
                
}
