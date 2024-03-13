package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByNameContaining(String partOfName);
    List<Book> findByAuthorContaining(String author);
    List<Book> findBooksByOrderByPriceAsc();
    List<Book> findBooksByOrderByPriceDesc();
    Optional<Book> findBookByName(String name);
}
