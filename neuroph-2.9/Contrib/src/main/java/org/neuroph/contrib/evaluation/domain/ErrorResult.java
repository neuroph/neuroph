package org.neuroph.contrib.evaluation.domain;

public class ErrorResult {

    private double error;

    ErrorResult(double error) {
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

        public ErrorResult createErrorMeasureResult() {
            return new ErrorResult(error);
        }
    }

}