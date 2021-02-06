package com.robsonc.libraryapi.api.dto;

/*
@Author Robson Coutinho 
Created on 06/02/2021 
*/


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnedLoanDTO {
    private Boolean returned;
}
