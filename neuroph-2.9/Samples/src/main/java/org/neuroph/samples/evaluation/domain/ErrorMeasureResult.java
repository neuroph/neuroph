package org.neuroph.samples.evaluation.domain;

public class ErrorMeasureResult {

    private double error;

    ErrorMeasureResult(double error) {
        this.error = error;
    }

    public double getError() {
        return error;
    }

    public void setErrorMeasure(double errorMeasure) {
        this.error = errorMeasure;
    }

    public static class ErrorMeasureResultBuilder {
        private double error;

        public ErrorMeasureResultBuilder withError(double error) {
            this.error = error;
            return this;
        }

        public ErrorMeasureResult createErrorMeasureResult() {
            return new ErrorMeasureResult(error);
        }
    }

}