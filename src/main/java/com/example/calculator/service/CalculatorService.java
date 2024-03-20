package com.example.calculator.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculate(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression)
                    .build();
            return exp.evaluate();
        } catch (Exception e) {
            // Log the error or throw a custom exception as appropriate
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }
    }
}

