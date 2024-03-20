package com.example.calculator.model;

public class CalculationResponse {
    private double result;

    // Constructor, getter and setter
    public CalculationResponse(double result) {
        this.result = result;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}