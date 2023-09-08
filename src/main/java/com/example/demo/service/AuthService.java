package com.example.demo.service;

import com.example.demo.dto.CredentialDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<UserDto> login(CredentialDto credentialsDto);

    UserDto register(SignUpDto userDto);

    String getRequestTokenForUser(String username);

    UserDto findByLogin(String login);

    void setRequestTokenForUser(String token , String username);
}
