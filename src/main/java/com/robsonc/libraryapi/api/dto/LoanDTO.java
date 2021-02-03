package com.robsonc.libraryapi.api.dto;

/*
@Author Robson Coutinho 
Created on 31/01/2021 
*/


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoanDTO {

    private String isbn;
    private String customer;
}
