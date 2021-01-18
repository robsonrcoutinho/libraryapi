package com.robsonc.libraryapi.api.resouce;

import com.robsonc.libraryapi.api.dto.BookDTO;
import com.robsonc.libraryapi.api.exceptions.ApiErrors;
import com.robsonc.libraryapi.entity.Book;
import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        /*Book entity = Book.builder()
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .build();
        entity = service.save(entity);

        return BookDTO.builder()
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .title(entity.getTitle())
                .id(entity.getId())
                .build();*/
        return modelMapper.map(entity, BookDTO.class);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }



}