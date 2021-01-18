package com.robsonc.libraryapi.service.impl;

import com.robsonc.libraryapi.entity.Book;
import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.model.repository.BookRepository;
import com.robsonc.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn ja cadastrado.");

        }
        return repository.save(book);
    }
}
