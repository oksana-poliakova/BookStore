package com.example.bookstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author oksanapoliakova on 13.03.2024
 * @projectName BookStore
 */

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    /** TODO: Write tests */
    @Test
    void testBookNameIsNotValid() throws Exception {
    }

    @Test
    void testCreateBookValid() throws Exception {

    }
}