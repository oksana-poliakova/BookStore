package com.example.bookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author oksanapoliakova on 13.03.2024
 * @projectName BookStore
 */

@Constraint(validatedBy = BookNameUniqueValidator.class)
@Target(ElementType.FIELD) // it can be applied to the field, TYPE - to the class
@Retention(RetentionPolicy.RUNTIME) // retention policy for this annotation
public @interface BookNameUnique {

    String message() default "A book with this name already exists";

    // Specifies the validation groups to which this constraint belongs.
    Class<?>[] groups() default {};

    // Payloads are additional information that can be attached to a constraint declaration.
    Class<? extends Payload>[] payload() default {};
}
