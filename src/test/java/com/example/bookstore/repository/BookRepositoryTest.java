//package com.example.bookstore.repository;
//
//import com.example.bookstore.entity.Book;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author oksanapoliakova on 13.03.2024
// * @projectName BookStore
// */
//
//@DataJpaTest
//class BookRepositoryTest {
//    @Autowired
//    BookRepository bookRepository;
//
//    @Test
//    void testSave() {
//        var book = new Book();
//        book.setName("Book4");
//
//        var savedBook = bookRepository.save(book);
//
//        var retrievedBook = bookRepository.findById(savedBook.getId());
//
//        assertTrue(retrievedBook.isPresent());
//    }
//}