package org.neuroph.contrib.model.evaluation.domain;


import java.util.PriorityQueue;

/**
 * Wrapper class used for ordering classification results
 */
public class ClassificationOutput implements Comparable<ClassificationOutput> {

    private int actualClass;
    private double outputValue;


    private ClassificationOutput(int actualClass, double outputValue) {
        this.actualClass = actualClass;
        this.outputValue = outputValue;
    }

    public int getActualClass() {
        return actualClass;
    }

    public double getOutputValue() {
        return outputValue;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ClassificationOutput))
            return false;
        ClassificationOutput that = (ClassificationOutput) obj;
        return this.getActualClass() == that.getActualClass();
    }

    public int compareTo(ClassificationOutput o) {
        if (getOutputValue() < o.getOutputValue()) {
            return 1;
        } else if (getOutputValue() > o.getOutputValue()) {
            return -1;
        } else
            return 0;
    }

    @Override
    public String toString() {
        String object = "ID: " + getActualClass() + ", Output: " + getOutputValue();
        return object;
    }


    /**
     *
     * @param results classification results computed by NeuralNetwork @see NeuralNetwork.getOutput
     * @return priority queue ordered by total results of each class in classifier
     */
    public static PriorityQueue<ClassificationOutput> orderedOutput(final double[] results) {
        PriorityQueue<ClassificationOutput> classificationOutput = new PriorityQueue<>();

        for (int i = 0; i < results.length; i++) {
            classificationOutput.add(new ClassificationOutput(i, results[i]));
        }
        return classificationOutput;
    }

    public static ClassificationOutput getMaxOutput(final double[] results) {
        return orderedOutput(results).peek();
    }


}


