package com.robsonc.libraryapi.api.dto;

/*
@Author Robson Coutinho 
Created on 06/02/2021 
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanFilterDTO {
    private String isbn;
    private String customer;
}
