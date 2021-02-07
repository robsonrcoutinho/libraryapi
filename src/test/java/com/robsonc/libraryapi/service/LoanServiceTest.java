package com.robsonc.libraryapi.service;

/*
@Author Robson Coutinho 
Created on 03/02/2021 
*/

import com.robsonc.libraryapi.api.dto.LoanFilterDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;
    @MockBean
    LoanRepository repository;


    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }


    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest() {
        String customer = "fulano";
        Book book = Book.builder().id(1l).build();
        Loan savingLoan = Loan.builder()
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
    public void loanedBookSaveTest() {
        String customer = "fulano";
        Book book = Book.builder().id(1l).build();
        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> service.save(savingLoan));


        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        verify(repository, Mockito.never()).save(savingLoan);
    }


    @Test
    @DisplayName("Deve obter as informações de emprestimo pelo id")
    public void getLoanDetailsTest() {
        //Cenario
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(loan));

        //Execução
        Optional<Loan> result = service.getById(id);

        //verificacao
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(repository).findById(id);
    }


    @Test
    @DisplayName("Deve atualizar um emprestimo")
    public void updateLoanTest() {
        Long id = 1l;
        Loan loan = Loan.builder().id(id).build();
        loan.setReturned(true);
        loan.setId(id);

        when(repository.save(loan)).thenReturn(loan);

        Loan loanUpdated = service.update(loan);

        assertThat(loanUpdated.getReturned()).isTrue();

        verify(repository).save(loan);
    }


    @Test
    @DisplayName("Deve filtrar emprestimos pelas propriedades")
    public void findLoanTest() {

        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("fulando").isbn("321").build();

        Loan loan = createLoan();
        loan.setId(1l);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> list = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<Loan>(list, pageRequest, list.size());

        when(repository.findByBookIsbnOrCustomer(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(PageRequest.class)))
                .thenReturn(page);
        
        //Execution
        service.find(loanFilterDTO, pageRequest);

        Page<Loan> result = service.find(loanFilterDTO, pageRequest);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Arrays.asList(loan));
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }


    public static Loan createLoan() {
        String customer = "fulano";
        Book book = Book.builder().id(1l).build();
        return Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
    }


}
