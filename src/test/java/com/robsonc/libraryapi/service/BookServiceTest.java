package com.robsonc.libraryapi.service;


import com.robsonc.libraryapi.model.entity.Book;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("55555555500").title("Um estudo em vermelho").author("Arthur Conan Doile").build());

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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn ja cadastrado.");
        Mockito.verify(repository, Mockito.never()).save(book);
    }


    @Test
    @DisplayName("Deve obter informações de um livro pelo ID")
    public void getByIdTest(){
        //Cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        //Execução
        Optional<Book> foundBook = service.getById(id);

        //Verificações

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
    }


    @Test
    @DisplayName("Deve retornar vazio quando um livro buscado pelo ID não for encontrado")
    public void bookNotFoundGetByIdTest(){
        //Cenario
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        //Execução
        Optional<Book> book = service.getById(id);

        //Verificações
        assertThat(book.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve remover um livro")
    public void deleteBookTest(){
        //Cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);

        //Execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> service.delete(book));

        //Verificar
        Mockito.verify(repository, times(1)).delete(book);
    }


    @Test
    @DisplayName("Deve retornar erro ao tentar remover um livro invalido")
    public void deleteInvalidBookTest(){
        //Cenario
        Long id = 1L;
        Book book = new Book();

        //Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        //Verificar
        Mockito.verify(repository, Mockito.never()).delete(book);

    }

    @Test
    @DisplayName("Deve lancar erro ao atualizar um livro invalido")
    public void updateInvalidBookTest(){
        //Cenario
        Long id = 1L;
        Book book = new Book();

        //Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        //Verificar
        Mockito.verify(repository, Mockito.never()).save(book);

    }


    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBook(){
        Long id = 1L;
        Book updatingBook = Book.builder().id(id).build();

        Book updatedBook = createValidBook();
        updatedBook.setId(id);

        when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.update(updatingBook);
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest(){
        Book book = createValidBook();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<Book>(Arrays.asList(book), pageRequest, 1);
        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);
        Page<Book> result = service.find(book, pageRequest);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Arrays.asList(book));
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void getBookByIsbnTest(){
        String isbn =  "0000";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1l).isbn(isbn).build()));

        Optional<Book> book = service.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1l);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        Mockito.verify(repository, times(1)).findByIsbn(isbn);

    }


    private Book createValidBook() {
        return Book.builder().author("Arthur Conan Doile").title("Um estudo em vermelho").isbn("55555555500").build();
    }

}
