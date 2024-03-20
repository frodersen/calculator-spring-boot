package com.example.calculator.controller;

import com.example.calculator.model.Calculation;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResponse;
import com.example.calculator.model.User;
import com.example.calculator.repository.CalculationRepository;
import com.example.calculator.repository.UserRepository;
import com.example.calculator.security.JwtUtil;
import com.example.calculator.service.CalculatorService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
@RequestMapping
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalculationRepository calculationRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @GetMapping("/calculations")
    public List<Calculation> getCalculations(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(jwtUtil.resolveToken(request));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return calculationRepository.findByUserOrderByCreatedDesc(user, PageRequest.of(page, size));
    }

    @PostMapping("api/calculate")
    public ResponseEntity<?> calculate(@RequestBody CalculationRequest request, HttpServletRequest httpRequest) {
        logger.info("Received calculation request for expression: {}", request.getExpression());

        try {
            double result = calculatorService.calculate(request.getExpression());
            String username = jwtUtil.getUsernameFromToken(jwtUtil.resolveToken(httpRequest));
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            
            // Create and save the calculation
            Calculation calculation = new Calculation();
            calculation.setExpression(request.getExpression());
            calculation.setResult(String.valueOf(result));
            calculation.setUser(user);
            calculationRepository.save(calculation);
            
            return ResponseEntity.ok(new CalculationResponse(result));
        } catch (IllegalArgumentException e) {
            logger.error("Error calculating expression: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
