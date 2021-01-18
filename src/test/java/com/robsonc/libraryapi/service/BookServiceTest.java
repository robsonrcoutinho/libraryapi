package com.robsonc.libraryapi.service;


import com.robsonc.libraryapi.entity.Book;
import com.robsonc.libraryapi.exceptions.BusinessException;
import com.robsonc.libraryapi.model.repository.BookRepository;
import com.robsonc.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o livro")
    public void savedBookTest() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("55555555500").title("Um estudo em vermelho").author("Arthur Conan Doile").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("55555555500");
        assertThat(savedBook.getAuthor()).isEqualTo("Arthur Conan Doile");
        assertThat(savedBook.getTitle()).isEqualTo("Um estudo em vermelho");

    }

    @Test
    @DisplayName("Deve lancar erro de negocio com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN() {
        Book book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn ja cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);


    }


    private Book createValidBook() {
        return Book.builder().author("Arthur Conan Doile").title("Um estudo em vermelho").isbn("55555555500").build();
    }

}
