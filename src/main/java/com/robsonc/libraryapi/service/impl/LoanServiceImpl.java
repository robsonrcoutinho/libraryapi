package com.robsonc.libraryapi.service.impl;

import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.model.entity.Loan;
import com.robsonc.libraryapi.model.repository.LoanRepository;
import com.robsonc.libraryapi.service.LoanService;

import java.util.Optional;

/*
@Author Robson Coutinho
Created on 04/02/2021
*/
public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void update(Loan loan) {
        repository.save(loan);
    }
}
