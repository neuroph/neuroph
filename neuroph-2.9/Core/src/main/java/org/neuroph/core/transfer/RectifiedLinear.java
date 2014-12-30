package org.neuroph.core.transfer;

public class RectifiedLinear extends TransferFunction {


    @Override
    public double getOutput(double net) {
//        return Math.min(Math.max(0, net), 100);
        return Math.max(0, net);

    }


    public double getDerivative(double net) {
        if (net > Double.MIN_VALUE)
            return 1;
        return 0;
    }

}
