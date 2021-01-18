package com.robsonc.libraryapi.model.repository;

import com.robsonc.libraryapi.api.dto.BookDTO;
import com.robsonc.libraryapi.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

        Book book = Book.builder().author("Arthur Conan Doile").title("Um estudo em vermelho").isbn(isbn).build();
        entityManager.persist(book);
        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();

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

}
