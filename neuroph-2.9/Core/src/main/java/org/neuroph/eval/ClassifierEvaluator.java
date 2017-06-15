package org.neuroph.eval;

import org.neuroph.eval.classification.ConfusionMatrix;
import org.neuroph.eval.classification.Utils;


public abstract class ClassifierEvaluator implements Evaluator<ConfusionMatrix> {

    private final String[] classLabels;    
    private double threshold; // used for binary classifier but should also be used for multi class
    ConfusionMatrix confusionMatrix;

       
    private ClassifierEvaluator(String[] labels) {
        this.classLabels = labels;
        confusionMatrix = new ConfusionMatrix(labels);
    }

    @Override
    public ConfusionMatrix getResult() {
        return confusionMatrix;
    }


    public double getThreshold() {
        return threshold;
    }

    public final void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public void reset() {
        confusionMatrix = new ConfusionMatrix(classLabels);
    }
    

    /**
     * Binary evaluator used for computation of metrics in case when data has only one output result (one output neuron)
     */
    public static final class Binary extends ClassifierEvaluator {

        public static final String[] CLASS_LABELS = new String[]{"True", "False"}; 
              
        public Binary(double threshold) {
            super(CLASS_LABELS);
            setThreshold(threshold);
        }

        @Override
        public void processNetworkResult(double[] networkOutput, double[] desiredOutput) {
            
            boolean actualClass = (desiredOutput[0] > 0); // true id desired output is 1, false otherwise
            boolean predictedClass = (networkOutput[0] > getThreshold()); // true if actual output is >= threshold, false otherwise
                                    
            if (actualClass && predictedClass) {
                confusionMatrix.incrementElement(0, 0); // tp
            } else if (actualClass && !predictedClass) {
                confusionMatrix.incrementElement(0, 1); // fn
            } else if (!actualClass && predictedClass) {
                confusionMatrix.incrementElement(1, 0); // fp
            } else if (!actualClass && !predictedClass) {
                confusionMatrix.incrementElement(1, 1); // tn
            }

        }
    }

    /**
     * Evaluator used for computation of metrics in case when data has
     * multiple classes - one vs many classification
     */
    public static class MultiClass extends ClassifierEvaluator {

        // TODO: use column labels here
        public MultiClass(String[] classLabels) {            
            super(classLabels);
            // dataSet.getColumnNames()
        }

        @Override
        public void processNetworkResult(double[] predictedOutput, double[] actualOutput) {
            // just get max index
            int actualClassIdx = Utils.maxIdx(actualOutput);
            int predictedClassIdx = Utils.maxIdx(predictedOutput);
            // TODO: use threshold here not only max
            
            confusionMatrix.incrementElement(actualClassIdx, predictedClassIdx);
        }
    }

}
