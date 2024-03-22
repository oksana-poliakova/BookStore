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
@RequestMapping("/api/books")
@Validated
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a book to the database")
    public ResponseEntity<Void> addBook(@Valid @RequestBody InsertBookDTO insertBookDTO) {
        bookService.mapToDTO(bookService.saveBook(insertBookDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Get a book by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookDTO> getBookById(@PathVariable("bookId") UUID bookId) {
        var book = bookService.getBookById(bookId);
        var retrievedBook = bookService.mapToDTO(book.get());
        return new ResponseEntity<>(retrievedBook, HttpStatus.OK);
    }

    @GetMapping("/getAllBooks")
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        var books = bookService.getAllBooks();
        var allBooks = books.stream().map(bookService::mapToDTO).toList();
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @PutMapping("/update/{bookId}")
    @Operation(summary = "Update a book by ID")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable("bookId") UUID bookId, @Valid InsertBookDTO insertBookDTO) {
        var updatedBook = bookService.mapToDTO(bookService.updateBookById(bookId, insertBookDTO).get());
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @GetMapping("/searchBooksByName")
    @Operation(summary = "Search books by name")
    public ResponseEntity<List<Book>> searchBooksByName(@RequestParam String partOfName) {
        var books = bookService.findBooksByNameContaining(partOfName);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/getBookByName")
    @Operation(summary = "Get book by name")
    public ResponseEntity<BookDTO> getBookByName(@RequestParam String name) {
        var book = bookService.mapToDTO(bookService.findBookByName(name).orElseThrow(() -> new EntityNotFoundException("Book with this name not found")));
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/searchByAuthor")
    @Operation(summary = "Search books by author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
        var books = bookService.findBooksByAuthorContaining(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/sortByPriceAsc")
    @Operation(summary = "Sort books by price (ascending)")
    public ResponseEntity<List<Book>> sortBooksByPriceAsc() {
        var sortedBooks = bookService.findBooksByOrderByPriceAsc();
        return new ResponseEntity<>(sortedBooks, HttpStatus.OK);
    }

    @GetMapping("/sortByPriceDesc")
    @Operation(summary = "Sort books by price (descending)")
    public ResponseEntity<List<Book>> sortBooksByPriceDesc() {
        var sortedBooks = bookService.findBooksByOrderByPriceDesc();
        return new ResponseEntity<>(sortedBooks, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bookId}")
    @Operation(summary = "Delete a book by ID")
    public ResponseEntity<Void> deleteBookById(@PathVariable("bookId") UUID bookId) {
        bookService.deleteById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
