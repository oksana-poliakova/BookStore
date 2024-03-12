package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Book> updateBookById(UUID bookId, InsertBookDTO updatedBookDTO) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();

            existingBook.setName(updatedBookDTO.getName());
            existingBook.setAuthor(updatedBookDTO.getAuthor());
            existingBook.setDescription(updatedBookDTO.getDescription());
            existingBook.setPrice(updatedBookDTO.getPrice());

            return Optional.of(bookRepository.save(existingBook));
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }


    public BookDTO mapToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
