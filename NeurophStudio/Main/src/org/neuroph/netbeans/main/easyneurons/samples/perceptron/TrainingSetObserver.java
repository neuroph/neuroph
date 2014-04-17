package org.neuroph.netbeans.main.easyneurons.samples.perceptron;

import java.util.Observable;
import java.util.Observer;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author Marko Koprivica
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class TrainingSetObserver implements Observer
{
    private DataSet trainingSet;

    public DataSet getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(DataSet trainingSet) {
        this.trainingSet = trainingSet;
    }

    @Override
    public void update(Observable o, Object arg) {
        setTrainingSet(((org.neuroph.netbeans.classificationsample.PerceptronSampleTrainingSet) o).getTrainingSet());
    }

}
