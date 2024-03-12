package com.example.bookstore.dto;

import lombok.Data;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */
@Data
public class InsertBookDTO {
    private String name;
    private String author;
    private String description;
    private Double price;
}
