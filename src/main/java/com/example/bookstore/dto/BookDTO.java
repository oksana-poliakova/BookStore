package com.example.bookstore.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */
@Data
public class BookDTO {
    private UUID id;
    private String name;
    private String author;
    private String description;
    private Double price;
}
