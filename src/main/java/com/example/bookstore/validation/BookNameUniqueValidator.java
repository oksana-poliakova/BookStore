package com.example.bookstore.validation;

import com.example.bookstore.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author oksanapoliakova on 13.03.2024
 * @projectName BookStore
 */

@AllArgsConstructor
public class BookNameUniqueValidator implements ConstraintValidator<BookNameUnique, String> {

    @Autowired
    private final BookRepository bookRepository;
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return bookRepository.findBookByName(name).isEmpty();
    }
}
