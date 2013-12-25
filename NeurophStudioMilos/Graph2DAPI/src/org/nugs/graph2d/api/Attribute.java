package org.nugs.graph2d.api;

/**
 * 
 * @author Vedrana Gajic
 */
public class Attribute {

    private boolean isOutput;
    private int index;
    private String label;

    public Attribute() {
    }

    /**
     * 
     * @param index - index of attribute
     * @param isOutput - if attribute represents output, isOutput is true, otherwise false
     * @param label - label of attribute (Example: "Input")
     */
    public Attribute(int index, boolean isOutput, String label) {
        this.isOutput = isOutput;
        this.index = index;
        this.label = label + " " + (index + 1);
    }

    public boolean isOutput() {
        return isOutput;
    }

    public void setIsOutput(boolean isOutput) {
        this.isOutput = isOutput;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
