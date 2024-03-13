package com.example.bookstore.service;

import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.factory.BookFactory;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author oksanapoliakova on 13.03.2024
 * @projectName BookStore
 */

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    BookService bookService;

    @BeforeEach
    void init() {
        bookService = new BookService(bookRepository, modelMapper);
    }

    @Test
    void getAllBooksSuccess() {
        List<Book> books = BookFactory.createBooks();

        when(bookRepository.findAll()).thenReturn(books);
        var result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).getName());
        assertEquals("Author1", result.get(0).getAuthor());
        assertEquals("Description1", result.get(0).getDescription());
        assertEquals(10.00, result.get(0).getPrice());

        assertEquals("Book2", result.get(1).getName());
        assertEquals("Book2", result.get(1).getName());
        assertEquals("Author2", result.get(1).getAuthor());
        assertEquals("Description2", result.get(1).getDescription());
        assertEquals(20.00, result.get(1).getPrice());
    }

    @Test
    public void testSaveBook() {
        InsertBookDTO insertBookDTO = new InsertBookDTO();
        Book book = new Book();
        when(modelMapper.map(insertBookDTO, Book.class)).thenReturn(book);
        when(bookRepository.saveAndFlush(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(insertBookDTO);

        assertNotNull(savedBook);
        verify(modelMapper, times(1)).map(insertBookDTO, Book.class);
        verify(bookRepository, times(1)).saveAndFlush(book);
    }

    @Test
    public void testGetBookById() {
        UUID bookId = UUID.randomUUID();
        Book book = new Book();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Optional<Book> retrievedBook = bookService.getBookById(bookId);

        assertTrue(retrievedBook.isPresent());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void findBooksByNameContaining() {
        Book book1 = BookFactory.createBook("Java Basic", "Herbert Schildt", "Java for beginners", 35.00);
        Book book2 = BookFactory.createBook("Java Advanced", "Bruce Eckel", "Java for advanced programmers", 45.00);
        Book book3 = BookFactory.createBook("test", "Bruce Eckel", "test for advanced programmers", 45.00);

        List<Book> booksContainingJava = Arrays.asList(book1, book2);
        List<Book> booksContainingTest = List.of(book3);

        when(bookRepository.findByNameContaining("Java")).thenReturn(booksContainingJava);
        when(bookRepository.findByNameContaining("test")).thenReturn(booksContainingTest);

        List<Book> actualBooksContainingJava = bookService.findBooksByNameContaining("Java");
        List<Book> actualBooksContainingTest = bookService.findBooksByNameContaining("test");

        assertEquals(2, actualBooksContainingJava.size());
        assertEquals(1, actualBooksContainingTest.size());
    }

}