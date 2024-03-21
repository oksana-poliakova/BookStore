package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author oksanapoliakova on 19.03.2024
 * @projectName BookStore
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userRepository);
    }

    @Test
    void getUserByIdIfExist() {
        UUID userId = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> retrievedUser = Optional.ofNullable(userService.getUserById(userId));

        assertTrue(retrievedUser.isPresent());
    }

    @Test
    void getUserByIdIfDoesntExist() {
        UUID userId = UUID.randomUUID();
        User user = new User();


        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals(noSuchElementException.getMessage(), "No value present");
    }

}
