package com.robsonc.libraryapi.service;

import com.robsonc.libraryapi.model.entity.Loan;

import java.util.List;
import java.util.Optional;

/*
@Author Robson Coutinho 
Created on 31/01/2021 
*/
public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    void update(Loan loan);
}
