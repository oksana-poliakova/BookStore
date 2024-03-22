package com.example.bookstore.service;

import com.example.bookstore.dto.book.BookDTO;
import com.example.bookstore.dto.book.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.factory.BookFactory;
import com.example.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    @Mock
    UserService userService;

    @InjectMocks
    BookService bookService;

    @BeforeEach
    void init() {
        bookService = new BookService(bookRepository,modelMapper,userService);
    }

    @Test
    void getAllBooksSuccess() {
        List<Book> books = BookFactory.createBooks();

        when(bookRepository.findAll()).thenReturn(books);
        var result = bookService.getAllBooks();

        assertEquals(4, result.size());
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
    public void testUpdateBookById() {
        UUID bookId = UUID.randomUUID();
        InsertBookDTO updatedBookDTO = new InsertBookDTO();
        updatedBookDTO.setName("Updated Book");
        updatedBookDTO.setAuthor("Updated Author");
        updatedBookDTO.setDescription("Updated Description");
        updatedBookDTO.setPrice(20.00);

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setName("Existing Book");
        existingBook.setAuthor("Existing Author");
        existingBook.setDescription("Existing Description");
        existingBook.setPrice(10.00);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Optional<Book> updatedBook = bookService.updateBookById(bookId, updatedBookDTO);

        assertTrue(updatedBook.isPresent());
        assertEquals(updatedBookDTO.getName(), updatedBook.get().getName());
        assertEquals(updatedBookDTO.getAuthor(), updatedBook.get().getAuthor());
        assertEquals(updatedBookDTO.getDescription(), updatedBook.get().getDescription());
        assertEquals(updatedBookDTO.getPrice(), updatedBook.get().getPrice());
    }

    @Test
    public void testUpdateBookById_InvalidId() {
        UUID invalidBookId = UUID.randomUUID();
        InsertBookDTO updatedBookDto = new InsertBookDTO();

        updatedBookDto.setName("Updated Title");

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBookById(invalidBookId, updatedBookDto);
        });

        assertEquals(entityNotFoundException.getMessage(), "Invalid id: " + invalidBookId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void testUpdateBookById_NullId() {
        InsertBookDTO updatedBookDto = new InsertBookDTO();
        updatedBookDto.setName("Updated Title");

        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            bookService.updateBookById(null, updatedBookDto);
        });

        assertNotNull(nullPointerException);
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void testFindBooksByNameContaining() {
        Book book1 = BookFactory.createBook("Java Basic", "Herbert Schildt", "Java for beginners", 35.00);
        Book book2 = BookFactory.createBook("Java Advanced", "Bruce Eckel", "Java for advanced programmers", 45.00);
        Book book3 = BookFactory.createBook("test", "Bruce Eckel", "test for advanced programmers", 45.00);
        Book book4 = BookFactory.createBook("Clean Code", "Robert C. Martin", "A Handbook of Agile Software Craftsmanship", 50.00);

        List<Book> booksContainingJava = Arrays.asList(book1, book2);
        List<Book> booksContainingTest = List.of(book3);
        List<Book> allBooks = Arrays.asList(book1, book2, book3, book4);

        when(bookRepository.findByNameContaining("Java")).thenReturn(booksContainingJava);
        when(bookRepository.findByNameContaining("test")).thenReturn(booksContainingTest);
        when(bookRepository.findByNameContaining("")).thenReturn(allBooks); // all books

        List<Book> actualBooksContainingJava = bookService.findBooksByNameContaining("Java");
        List<Book> actualBooksContainingTest = bookService.findBooksByNameContaining("test");
        List<Book> actualAllBooks = bookService.findBooksByNameContaining("");

        assertEquals(2, actualBooksContainingJava.size());
        assertEquals(1, actualBooksContainingTest.size());
        assertEquals(4, actualAllBooks.size());
    }

    @Test
    public void testDeleteById() {
        Book book = new Book();
        UUID bookId = UUID.randomUUID();

        lenient().when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteById_InvalidId() {
        assertThrows(NullPointerException.class, () -> {
            bookService.deleteById(null);
        });

        verify(bookRepository, never()).deleteById(null);
    }

    @Test
    public void testFindBookByName() {
        String title = "Title";
        Book expectedBook = new Book();
        expectedBook.setName(title);

        when(bookRepository.findBookByName(title)).thenReturn(Optional.of(expectedBook));
        Optional<Book> foundBookOptional = bookService.findBookByName(title);

        assertTrue(foundBookOptional.isPresent());
        assertEquals(expectedBook, foundBookOptional.get());
        verify(bookRepository, times(1)).findBookByName(title);
    }

    @Test
    public void testFindBooksByAuthorContaining() {
        Book book1 = BookFactory.createBook("Java Basic", "Test", "Java for beginners", 35.00);
        Book book2 = BookFactory.createBook("Java Advanced", "Bruce Eckel", "Java for advanced programmers", 45.00);
        Book book3 = BookFactory.createBook("Test", "Test", "test for advanced programmers", 45.00);
        Book book4 = BookFactory.createBook("Clean Code", "Robert C. Martin", "A Handbook of Agile Software Craftsmanship", 50.00);
        Book book5 = BookFactory.createBook("Java Basic", "Bruce Eckel", "Java for beginner programmers", 35.00);

        List<Book> booksContainingTestAuthor = Arrays.asList(book1, book5);

        when(bookRepository.findByAuthorContaining("Test")).thenReturn(booksContainingTestAuthor);

        List<Book> actualBooksContainingTestAuthor = bookService.findBooksByAuthorContaining("Test");

        assertEquals(2, booksContainingTestAuthor.size());
    }

    @Test
    public void testFindBooksByOrderByPriceAsc() {
        List<Book> books = BookFactory.createBooks();

        when(bookRepository.findAllByOrderByPriceAsc()).thenReturn(books);

        List<Book> actualBooks = bookService.findBooksByOrderByPriceAsc();

        assertEquals(books, actualBooks);
    }

    @Test
    public void testFindBooksByOrderByPriceDesc() {
        List<Book> books = BookFactory.createBooks();

        when(bookRepository.findAllByOrderByPriceDesc()).thenReturn(books);

        List<Book> actualBooks = bookService.findBooksByOrderByPriceDesc();

        assertEquals(books, actualBooks);
    }

    @Test
    public void testMapToDTO() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");
        book.setPrice(10.00);

        BookDTO expectedBookDTO = new BookDTO();
        expectedBookDTO.setName("Test Book");
        expectedBookDTO.setAuthor("Test Author");
        expectedBookDTO.setDescription("Test Description");
        expectedBookDTO.setPrice(10.00);

        when(modelMapper.map(book, BookDTO.class)).thenReturn(expectedBookDTO);
        BookDTO actualBookDTO = bookService.mapToDTO(book);

        assertEquals(expectedBookDTO, actualBookDTO);
    }
}
