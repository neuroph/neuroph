package org.neuroph.contrib.eval.classification;

/**
 * Ovu klasu definitivno izabciti
 * KOristi se samo getMaxOutput koja uz to i potpuno nebulozna jer vraca  ClassificationResult
 * vidi samo zasta nam treba u McNemar
 * 
 * a pri tom uvek se poziva getActual
 * 
 * Wrapper class used for ordering classification results
 */
public class ClassificationResult {

    private int classIdx; // order of the actual class (output neuron order)
    private double neuronOutput; // coresponding neuron output number
    private String label; // class label: should be obtained with neuron.getLabel()

    // TODO: add neuron label to constructor 

    public ClassificationResult(int classIdx, double neuronOutput) {
        this.classIdx = classIdx;
        this.neuronOutput = neuronOutput;
    }

    public int getClassIdx() {
        return classIdx;
    }

    public double getNeuronOutput() {
        return neuronOutput;
    }

    public String getLabel() {
        return label;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ClassificationResult))
            return false;
        ClassificationResult that = (ClassificationResult) obj;
        return this.getClassIdx() == that.getClassIdx();
    }


    @Override
    public String toString() {
        String object = "ID: " + getClassIdx() + ", Output: " + getNeuronOutput();
        return object;
    }


    
//    /**
//     *
//     * @param results classification results computed by NeuralNetwork @see NeuralNetwork.getOutput
//     * @return priority queue ordered by total results of each class in classifier
//     */
//    public static PriorityQueue<ClassificationResult> orderedOutput(final double[] results) {
//        PriorityQueue<ClassificationResult> classificationOutput = new PriorityQueue<>();
//
//        for (int i = 0; i < results.length; i++) {
//            classificationOutput.add(new ClassificationResult(i, results[i]));
//        }
//        return classificationOutput;
//    }
//
//    public static ClassificationResult fromMaxOutput(final double[] results) {
//        return orderedOutput(results).peek();
//    }


}