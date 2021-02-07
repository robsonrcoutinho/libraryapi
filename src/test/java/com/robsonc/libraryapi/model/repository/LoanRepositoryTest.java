package com.robsonc.libraryapi.model.repository;

/*
@Author Robson Coutinho 
Created on 04/02/2021 
*/


import com.robsonc.libraryapi.model.entity.Book;
import com.robsonc.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.robsonc.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository repository;

    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para o livro ")
    public void existsByBookAndNotReturnedTest(){
        Loan loan = createAndPersistLoan();
        Book book = loan.getBook();
        boolean exists = repository.existsByBookAndNotReturned(book);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve procurar emprestimo filtrando por isbn ou nome do customer")
    public void findByBookIsbnOrCustomerTest(){
        Loan loan = createAndPersistLoan();
        Page<Loan> result = repository
                .findByBookIsbnOrCustomer("123", "fulano", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);

    }

    public Loan createAndPersistLoan(){
        Book book = createNewBook("123");
        book = entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("fulano").loanDate(LocalDate.now()).build();

        entityManager.persist(loan);

        return loan;
    }




}
