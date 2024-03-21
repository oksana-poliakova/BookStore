package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDTO;
import com.example.bookstore.dto.book.InsertBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author oksanapoliakova on 21.03.2024
 * @projectName BookStore
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    BookService bookService;

    @Mock
    ModelMapper modelMapper;
    private static HttpHeaders headers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    BookController bookController;
    private MockMvc mockMvc; // like real request

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getAllBooksEndpointTest() throws Exception {
        mockMvc.perform(get("/api/book/getAllBooks")).andExpect(status().isOk());
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

        when(bookService.saveBook(any())).thenReturn(savedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(insertBookDTO)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.name").value(insertBookDTO.getName()))
                        .andExpect(jsonPath("$.author").value(insertBookDTO.getAuthor()))
                        .andExpect(jsonPath("$.description").value(insertBookDTO.getDescription()))
                        .andExpect(jsonPath("$.price").value(insertBookDTO.getPrice()));

        verify(bookService, times(1)).saveBook(any());
    }
}