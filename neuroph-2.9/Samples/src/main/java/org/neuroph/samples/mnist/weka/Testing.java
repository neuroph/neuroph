package org.neuroph.samples.mnist.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;


public class Testing {


    public static void main(String args[]) throws Exception {
//load data
        Instances data = new Instances(new BufferedReader(new
                FileReader("mnist")));
        data.setClassIndex(data.numAttributes() - 1);
//build model
        Logistic model = new Logistic();
        model.buildClassifier(data); //the last instance with missing
        System.out.println(model);
//classify the last instance
        Instance myHouse = data.lastInstance();
        double price = model.classifyInstance(myHouse);
        System.out.println("My house (" + myHouse + "): " + price);
    }
}

