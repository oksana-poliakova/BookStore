package com.example.bookstore.dto;

import com.example.bookstore.entity.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */

@Data
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private Role role;
}
