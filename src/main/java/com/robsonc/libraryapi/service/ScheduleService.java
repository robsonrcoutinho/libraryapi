package com.robsonc.libraryapi.service;

/*
@Author Robson Coutinho 
Created on 09/02/2021 
*/

import com.robsonc.libraryapi.model.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private final LoanService loanService;

    private final EmailService emailService;

    @Value("${send.mail.message}")
    private String message;

    @Scheduled(cron = CRON_LATE_LOANS, fixedDelay = 1000)
    public void sendMailToLateLoans(){
        List<Loan> allLateLoans = loanService.getAllLateLoans();

        List<String> mails =  allLateLoans.stream().map(loan ->
                loan.getCustomerEmail()).collect(Collectors.toList());

        emailService.sendMails(message, mails);



    }


}
