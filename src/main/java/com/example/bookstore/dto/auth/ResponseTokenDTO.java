package com.example.bookstore.dto.auth;

import com.example.bookstore.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTokenDTO {

    private UserDTO user;
    private String token;
    private Integer accessTokenValidTime;
}
