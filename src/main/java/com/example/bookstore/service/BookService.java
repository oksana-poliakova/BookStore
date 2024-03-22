package com.example.bookstore.service;

import com.example.bookstore.dto.book.BookDTO;
import com.example.bookstore.dto.book.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    public static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper, UserService userService) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    public Book saveBook(InsertBookDTO insertBookDTO) {
        var book = modelMapper.map(insertBookDTO, Book.class);

        return bookRepository.saveAndFlush(book);
    }

    public Optional<Book> getBookById(UUID bookId) {
        userService.getAuthentificatedUser();

        return bookRepository.findById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> updateBookById(UUID bookId, InsertBookDTO updatedBookDTO) {
        if (bookId == null) throw new NullPointerException();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Invalid id: " + bookId));

        if (updatedBookDTO.getName() != null) {
            book.setName(updatedBookDTO.getName());
        }
        if (updatedBookDTO.getAuthor() != null) {
            book.setAuthor(updatedBookDTO.getAuthor());
        }
        if (updatedBookDTO.getDescription() != null) {
            book.setDescription(updatedBookDTO.getDescription());
        }
        if (updatedBookDTO.getPrice() != null) {
            book.setPrice(updatedBookDTO.getPrice());
        }

        return Optional.of(bookRepository.save(book));
    }

    public Optional<Book> findBookByName(String name) {
        return bookRepository.findBookByName(name);
    }

    public List<Book> findBooksByNameContaining(String partOfName) {
        return bookRepository.findByNameContaining(partOfName);
    }

    public List<Book> findBooksByAuthorContaining(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    public List<Book> findBooksByOrderByPriceAsc() {
        return bookRepository.findBooksByOrderByPriceAsc();
    }

    public List<Book> findBooksByOrderByPriceDesc() {
        return bookRepository.findBooksByOrderByPriceDesc();
    }

    public void deleteById(UUID bookId) {
        if (bookId == null) throw new NullPointerException();
        bookRepository.deleteById(bookId);
    }

    public BookDTO mapToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
