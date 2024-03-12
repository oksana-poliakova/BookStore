package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
