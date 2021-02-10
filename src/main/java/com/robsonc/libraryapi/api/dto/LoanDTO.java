package com.robsonc.libraryapi.api.dto;

/*
@Author Robson Coutinho 
Created on 31/01/2021 
*/


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoanDTO {

    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String customer;
    private BookDTO book;
    @NotEmpty
    private String email;
}
