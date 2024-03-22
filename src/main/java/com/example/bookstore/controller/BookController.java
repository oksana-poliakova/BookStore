package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDTO;
import com.example.bookstore.dto.book.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */

@RestController
@RequestMapping("api/book")
@Validated
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Possibility to add a book to the database")
    public ResponseEntity<Void> save(@Valid @RequestBody InsertBookDTO insertBookDTO) {
        bookService.mapToDTO(bookService.saveBook(insertBookDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Possibility to get a book by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookDTO> getBookById(@PathVariable("bookId") UUID bookId) {
        var book = bookService.getBookById(bookId);
        var retrievedBook = bookService.mapToDTO(book.get());
        return new ResponseEntity<>(retrievedBook, HttpStatus.OK);
    }

    @GetMapping("/getAllBooks")
    @Operation(summary = "Possibility to get all books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        var books = bookService.getAllBooks();
        var allBooks = books.stream().map(bookService::mapToDTO).toList();
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @PutMapping("/updateBookById/{bookId}")
    @Operation(summary = "Possibility to update a book by id")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable("bookId") UUID bookId, @Valid InsertBookDTO insertBookDTO) {
        var updatedBook = bookService.mapToDTO(bookService.updateBookById(bookId, insertBookDTO).get());
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    @Operation(summary = "Possibility to search books by name")
    public ResponseEntity<List<Book>> searchBooksByName(@RequestParam String partOfName) {
        var books = bookService.findBooksByNameContaining(partOfName);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/getBookByName")
    @Operation(summary = "Possibility to get book by name")
    public ResponseEntity<BookDTO> getBookByName(@RequestParam String name) {
        var book = bookService.mapToDTO(bookService.findBookByName(name).orElseThrow(() -> new EntityNotFoundException("Book with this name not found")));
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/searchByAuthor")
    @Operation(summary = "Possibility to search books by author")
    public ResponseEntity<List<Book>> searchBookByAuthor(@RequestParam String author) {
        var books = bookService.findBooksByAuthorContaining(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/sortBookByPriceAsc")
    @Operation(summary = "Possibility to sort books by price asc")
    public ResponseEntity<List<Book>> findBooksByPriceAsc() {
        var sortedBooks = bookService.findBooksByOrderByPriceAsc();
        return new ResponseEntity<>(sortedBooks, HttpStatus.OK);
    }

    @GetMapping("/sortBookByPriceDesc")
    @Operation(summary = "Possibility to sort books by price asc")
    public ResponseEntity<List<Book>> findBooksByPriceDesc() {
        var sortedBooks = bookService.findBooksByOrderByPriceDesc();
        return new ResponseEntity<>(sortedBooks, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBookById/{bookId}")
    @Operation(summary = "Possibility to delete a book by id")
    public ResponseEntity<Void> deleteBookById(@PathVariable("bookId") UUID bookId) {
        bookService.deleteById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
