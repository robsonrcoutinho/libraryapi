package com.robsonc.libraryapi.service;

/*
@Author Robson Coutinho 
Created on 03/02/2021 
*/

import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.model.entity.Book;
import com.robsonc.libraryapi.model.entity.Loan;
import com.robsonc.libraryapi.model.repository.LoanRepository;
import com.robsonc.libraryapi.service.impl.LoanServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Mockito.when;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;
    @MockBean
    LoanRepository repository;


    @BeforeEach
    public void setUp(){
        this.service = new LoanServiceImpl(repository);
    }


    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest(){
        String customer = "fulano";
        Book book = Book.builder().id(1l).build();
        Loan savingLoan  = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())                
                .build();


        Loan loanSaved = Loan.builder()
                .id(1l)
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        when(repository.save(savingLoan)).thenReturn(loanSaved);

        Loan loan = service.save(savingLoan);


        assertThat(loan.getId()).isEqualTo(loanSaved.getId());
        assertThat(loan.getBook().getId()).isEqualTo(loanSaved.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(loanSaved.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(loanSaved.getLoanDate());


    }


    @Test
    @DisplayName("Deve lancar erro de negocio ao salvar um livro já emprestado")
    public void loanedBookSaveTest(){
        String customer = "fulano";
        Book book = Book.builder().id(1l).build();
        Loan savingLoan  = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> service.save(savingLoan));


        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        Mockito.verify(repository, Mockito.never()).save(savingLoan);




    }



}
