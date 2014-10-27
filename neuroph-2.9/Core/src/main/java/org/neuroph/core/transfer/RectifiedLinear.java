package org.neuroph.core.transfer;

public class RectifiedLinear extends TransferFunction {


    @Override
    public double getOutput(double net) {
        return Math.max(0, net);
    }


    public double getDerivative(double net) {
        if (net > 0)
            return 1;
        return 0;
    }

}
