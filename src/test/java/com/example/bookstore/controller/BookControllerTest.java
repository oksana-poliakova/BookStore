package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDTO;
import com.example.bookstore.dto.book.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author oksanapoliakova on 21.03.2024
 * @projectName BookStore
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    BookService bookService;

    @Mock
    ModelMapper modelMapper;
    private static HttpHeaders headers;
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    BookController bookController;
    private MockMvc mockMvc; // like real request

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllBooksEndpointTest() throws Exception {
        mockMvc.perform(get("/api/books/getAllBooks")).andExpect(status().isOk());
    }

    @Test
    void sortBookByPriceAscEndpointTest() throws Exception {
        mockMvc.perform(get("/api/books/sortByPriceAsc")).andExpect(status().isOk());
    }

    @Test
    void getBookByNameEndpointTest() throws Exception {
        String bookName = "Book2";
        Book book = new Book();
        book.setName(bookName);

        when(bookService.findBookByName(anyString())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/getBookByName")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookByAuthor() throws Exception {
        String author = "Author";
        Book book = new Book();
        book.setAuthor(author);
        List listOfBooks = List.of(book);

        when(bookService.findBooksByAuthorContaining(anyString())).thenReturn(listOfBooks);

        mockMvc.perform(get("/api/books/searchByAuthor")
                        .param("author", author)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findBookByPartOfNameEndpointTest() throws Exception {
        String bookName = "Book2";
        Book book = new Book();
        book.setName(bookName);
        List listOfBooks = List.of(book);

        when(bookService.findBooksByNameContaining(anyString())).thenReturn(listOfBooks);

        mockMvc.perform(get("/api/books/searchBooksByName")
                    .param("partOfName", bookName)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    void getBookByIdEndpointTest() throws Exception {
        UUID bookId = UUID.randomUUID();

        Book book = new Book();
        book.setId(bookId);

        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookByNameWhenNoParamTest() throws Exception {
        this.mockMvc.perform(get("/api/books/getBookByName"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sortBookByPriceDescEndpointTest() throws Exception {
        mockMvc.perform(get("/api/books/sortByPriceDesc"));
    }

    @Test
    void saveBookEndpointTest() throws Exception {
        InsertBookDTO insertBookDTO = new InsertBookDTO();
        insertBookDTO.setName("Sample Book");
        insertBookDTO.setAuthor("Sample Author");
        insertBookDTO.setDescription("Sample Description");
        insertBookDTO.setPrice(10.50);

        Book savedBook = new Book();
        savedBook.setId(UUID.randomUUID());
        savedBook.setName(insertBookDTO.getName());
        savedBook.setAuthor(insertBookDTO.getAuthor());
        savedBook.setDescription(insertBookDTO.getDescription());
        savedBook.setPrice(insertBookDTO.getPrice());

        String bookJson = objectMapper.writeValueAsString(savedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteBookByIdEndpointTest() throws Exception {
        UUID bookId = UUID.randomUUID();

        mockMvc.perform(delete("/api/books/delete/{bookId}", bookId))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(eq(bookId));
    }

}