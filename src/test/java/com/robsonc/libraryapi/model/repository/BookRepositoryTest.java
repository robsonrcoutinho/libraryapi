package com.robsonc.libraryapi.model.repository;

import com.robsonc.libraryapi.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;


    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado")
    public void shouldThueWhenIsbnExists(){
        String isbn = "123";

        Book book = createNewBook(isbn);
        entityManager.persist(book);
        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();

    }

    private Book createNewBook(String isbn) {
        return Book.builder().author("Arthur Conan Doile").title("Um estudo em vermelho").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar false quando  nao existir um livro na base com isbn informado")
    public void shouldFalseWhenIsbnDoesntExists(){
        //cenario
        String isbn = "123";

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //validacao
        assertThat(exists).isFalse();
    }
    
    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        Book book = createNewBook("123");
        book = entityManager.persist(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createNewBook("1234");
        Book savedBook = repository.save(book);
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        Book book = createNewBook("1234");
       entityManager.persist(book);

       Book bookFound = entityManager.find(Book.class, book.getId());

       repository.delete(bookFound);

        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }
}
