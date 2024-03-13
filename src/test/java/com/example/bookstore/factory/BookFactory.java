package com.example.bookstore.factory;

import com.example.bookstore.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oksanapoliakova on 13.03.2024
 * @projectName BookStore
 */
public class BookFactory {
    public static List<Book> createBooks() {
        List<Book> books = new ArrayList<>();

        Book book1 = createBook("Book1", "Author1", "Description1", 10.00);
        Book book2 = createBook("Book2", "Author2", "Description2", 20.00);
        Book book3 = createBook("Java Basic", "Herbert Schildt", "Java for beginners", 35.00);
        Book book4 = createBook("Java Advanced", "Bruce Eckel", "Java for advanced programmers", 45.00);

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        return books;
    }

    public static Book createBook(String name, String author, String description, double price) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        book.setPrice(price);
        return book;
    }
}
