package org.neuroph.samples.evaluation.domain;


import com.sun.org.apache.bcel.internal.generic.ClassObserver;

import java.util.PriorityQueue;

public class ClassificationOutput implements Comparable<ClassificationOutput> {

    private int classId;
    private double outputValue;

    public int getClassId() {
        return classId;
    }

    public double getOutputValue() {
        return outputValue;
    }


    private ClassificationOutput(int classId, double outputValue) {
        this.classId = classId;
        this.outputValue = outputValue;
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
        return this.getClassId() == that.getClassId();
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
        String object = "ID: " + getClassId() + ", Output: " + getOutputValue();
        return object;
    }

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


