package com.example.bookstore.dto;

import com.example.bookstore.validation.BookCreate;
import com.example.bookstore.validation.BookNameUnique;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */
@Data
public class BookDTO {
    private UUID id;
    @NotEmpty(message = "Book name can't be empty")
    @BookNameUnique(groups = BookCreate.class)
    private String name;
    @NotEmpty(message = "Author name can't be empty")
    private String author;
    @NotEmpty(message = "Description can't be empty")
    private String description;
    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "1.00", inclusive = true, message = "Price can't be lower than 1")
    private Double price;
}
