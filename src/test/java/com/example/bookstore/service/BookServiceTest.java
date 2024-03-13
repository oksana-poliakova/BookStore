package com.example.bookstore.service;

import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void getAllBooksSuccess() {
        var book1 = new Book();
        book1.setName("Book1");
        book1.setAuthor("Author1");
        book1.setDescription("Description1");
        book1.setPrice(10.00);

        var book2 = new Book();
        book2.setName("Book2");
        book2.setAuthor("Author2");
        book2.setDescription("Description2");
        book2.setPrice(20.00);

        var books = List.of(book1, book2);

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

    }



}