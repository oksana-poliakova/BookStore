package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "author")
    private String author;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
}
