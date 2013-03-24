/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.wizards;

/**
 *
 * @author Marko
 */
public class NewTrainingSetValuesWizard {

    private static NewTrainingSetValuesWizard instance;

    private int inputNumber;
    private int outputNumber;
    private String name;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static NewTrainingSetValuesWizard getInstance(){
        if(instance==null){
            instance = new NewTrainingSetValuesWizard();
        }
        return instance;
    }

    public int getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOutputNumber() {
        return outputNumber;
    }

    public void setOutputNumber(int outputNumber) {
        this.outputNumber = outputNumber;
    }
}
