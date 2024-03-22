package com.example.bookstore.service;

import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */
@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getAuthenticatedUser() {
        try {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findUserByUsername(username).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            throw new CustomException("User not authenticated", HttpStatus.FORBIDDEN.value());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userRepository.findUserByUsername(username).orElseThrow();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
