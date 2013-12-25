package org.neuroph.netbeans.stock.wizard;

/**
 *
 * @author Tomek
 */
public class TransferFunction {

    public TransferFunction(String functionName) {
        this.functionName = functionName;
    }

    private String functionName;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return getFunctionName();
    }


}
