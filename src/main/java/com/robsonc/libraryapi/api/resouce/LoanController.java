package com.robsonc.libraryapi.api.resouce;

/*
@Author Robson Coutinho 
Created on 29/01/2021 
*/

import com.robsonc.libraryapi.api.dto.LoanDTO;
import com.robsonc.libraryapi.api.dto.ReturnedLoanDTO;
import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.model.entity.Book;
import com.robsonc.libraryapi.model.entity.Loan;
import com.robsonc.libraryapi.service.BookService;
import com.robsonc.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();
        entity = service.save(entity);
        return entity.getId();
    }


    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());
        service.update(loan);

    }

}
