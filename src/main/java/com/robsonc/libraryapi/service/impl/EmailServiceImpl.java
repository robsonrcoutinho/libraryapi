package com.robsonc.libraryapi.service.impl;

import com.robsonc.libraryapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
@Author Robson Coutinho 
Created on 09/02/2021 
*/

@Service

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.default.remetent}")
    private String remetent;

    @Override
    public void sendMails(String message, List<String> mails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String[] to = mails.toArray(new String[mails.size()]);

        mailMessage.setFrom(remetent);
        mailMessage.setSubject("[Library API] - Devolva o livro Gael -- Livro com empréstimo atrasado. ");
        mailMessage.setText(message);
        mailMessage.setTo(to);
        javaMailSender.send(mailMessage);


    }
}
