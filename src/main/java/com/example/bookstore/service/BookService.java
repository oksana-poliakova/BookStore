package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
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
    public static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public Book saveBook(InsertBookDTO insertBookDTO) {
        var book = modelMapper.map(insertBookDTO, Book.class);

        return bookRepository.saveAndFlush(book);
    }

    public Optional<Book> getBookById(UUID bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> updateBookById(UUID bookId, InsertBookDTO updatedBookDTO) {
        try {
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isPresent()) {
                Book existingBook = optionalBook.get();

                if (updatedBookDTO.getName() != null) {
                    existingBook.setName(updatedBookDTO.getName());
                }
                if (updatedBookDTO.getAuthor() != null) {
                    existingBook.setAuthor(updatedBookDTO.getAuthor());
                }
                if (updatedBookDTO.getDescription() != null) {
                    existingBook.setDescription(updatedBookDTO.getDescription());
                }
                if (updatedBookDTO.getPrice() != null) {
                    existingBook.setPrice(updatedBookDTO.getPrice());
                }

                return Optional.of(bookRepository.save(existingBook));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error updating book with ID " + bookId, e);
            throw new RuntimeException("Error updating book with ID " + bookId, e);
        }
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }


    public BookDTO mapToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
