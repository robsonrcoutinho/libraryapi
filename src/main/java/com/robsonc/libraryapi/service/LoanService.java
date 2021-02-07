package com.robsonc.libraryapi.service;

import com.robsonc.libraryapi.api.dto.LoanFilterDTO;
import com.robsonc.libraryapi.model.entity.Book;
import com.robsonc.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/*
@Author Robson Coutinho 
Created on 31/01/2021 
*/
public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filter, Pageable pageRequest);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
}
