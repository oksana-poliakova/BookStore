package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    @Operation(summary = "Possibility to add a book to the database")
    public BookDTO save(@Valid @RequestBody InsertBookDTO insertBookDTO) {
        return bookService.mapToDTO(bookService.saveBook(insertBookDTO));
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Possibility to get a book by id")
    public BookDTO getBookById(@PathVariable("bookId") UUID bookId) {
        var book = bookService.getBookById(bookId);
        return bookService.mapToDTO(book.get());
    }

    @GetMapping("/getAllBooks")
    @Operation(summary = "Possibility to get all books")
    public List<BookDTO> getAllBooks() {
        var books = bookService.getAllBooks();
        return books.stream().map(bookService::mapToDTO).toList();
    }

    @PutMapping("/updateBookById/{bookId}")
    @Operation(summary = "Possibility to update a book by id")
    public BookDTO updateBookById(@PathVariable("bookId") UUID bookId, InsertBookDTO insertBookDTO) {
        return bookService.mapToDTO(bookService.updateBookById(bookId, insertBookDTO).get());
    }

    @DeleteMapping("/deleteBookById")
    @Operation(summary = "Possibility to delete a book by id")
    public void deleteBookById(UUID bookId) {
        bookService.deleteById(bookId);
    }
}
