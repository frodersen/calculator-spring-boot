package com.example.calculator.controller;

import com.example.calculator.model.Calculation;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResponse;
import com.example.calculator.model.User;
import com.example.calculator.repository.CalculationRepository;
import com.example.calculator.service.CalculatorService;
import com.example.calculator.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
@RequestMapping("/api/calculate")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;
    private LoginService loginService;
    private CalculationRepository calculationRepository;

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @GetMapping("/calculations")
    public List<Calculation> getCalculations(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        // Assuming you have a method to get the currently authenticated user
        User user = loginService.getCurrentAuthenticatedUser();
        return calculationRepository.findByUserOrderByCreatedDesc(user, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<?> calculate(@RequestBody CalculationRequest request) {
        // Log the received expression
        logger.info("Received calculation request for expression: {}", request.getExpression());

        try {
            double result = calculatorService.calculate(request.getExpression());
            return ResponseEntity.ok(new CalculationResponse(result));
        } catch (IllegalArgumentException e) {
            logger.error("Error calculating expression: {}", e.getMessage());
            // Return a 400 Bad Request with the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}