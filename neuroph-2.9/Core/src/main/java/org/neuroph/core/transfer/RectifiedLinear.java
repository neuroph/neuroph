package org.neuroph.core.transfer;

public class RectifiedLinear extends TransferFunction {
	private static final long serialVersionUID = 1L;

	@Override
    public double getOutput(double net) {
        return Math.max(0, net);
    }

    public double getDerivative(double net) {
        if (net > Double.MIN_VALUE)
            return 1;
        return 0;
    }

}
