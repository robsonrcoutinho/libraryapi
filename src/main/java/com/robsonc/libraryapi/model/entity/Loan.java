package com.robsonc.libraryapi.model.entity;

/*
@Author Robson Coutinho 
Created on 31/01/2021 
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*@Entity
@Table*/
public class Loan {

    private Long id;
    private String customer;
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;
}
