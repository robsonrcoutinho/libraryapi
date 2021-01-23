package com.robsonc.libraryapi.service;

import com.robsonc.libraryapi.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);
    Optional<Book> getById(Long id);
}
