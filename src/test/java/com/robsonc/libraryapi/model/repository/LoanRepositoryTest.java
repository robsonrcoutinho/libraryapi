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
        Book book = createNewBook("123");
        book = entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("fulano").loanDate(LocalDate.now()).build();

        entityManager.persist(loan);

        boolean exists = repository.existsByBookAndNotReturned(book);
        
        assertThat(exists).isTrue();


    }


}
