package com.example.calculator.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.calculator.model.Calculation;
import com.example.calculator.model.User;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    List<Calculation> findByUserOrderByCreatedDesc(User user, PageRequest pageRequest);
}