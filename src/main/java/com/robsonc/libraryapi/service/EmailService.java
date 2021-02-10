package com.robsonc.libraryapi.service;

import java.util.List;

public interface EmailService {
    void sendMails(String message, List<String> mails);
}
