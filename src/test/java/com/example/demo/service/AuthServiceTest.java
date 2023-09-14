package com.example.demo.service;

import com.example.demo.dto.CredentialDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthServiceImpl authService;

    @Test
    @Order(1)
    void register() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("user@email.com");
        signUpDto.setFirstName("FName");
        signUpDto.setFirstName("LName");
        signUpDto.setContact("0123456788");
        signUpDto.setPassword("password".toCharArray());
        signUpDto.setCountry("Sri Lanka");
        signUpDto.setBusinessName("Business Name");
        assertEquals(200, authService.register(signUpDto).getStatusCodeValue());
    }

    @Test
    @Order(2)
    void login() {
        System.out.println("running test 1");
        CredentialDto dto = new CredentialDto();
        dto.setUsername("user@email.com");
        dto.setPassword("password".toCharArray());
        assertEquals(200 , authService
                .login(dto)
                .getStatusCode().value());
    }

    @Test
    @Order(3)
    void findByLogin() {
        System.out.println(authService.findByLogin("user@email.com"));
    }

//    @Test
//    @Order(4)
//    void getRequestTokenForUser() {
//        assertNotNull(authService.getRequestTokenForUser("user@email.com"));
//    }

    @Test
    @Order(4)
    void deleteUser(){
        assertEquals(200, authService.deleteUser("user@email.com").getStatusCodeValue());
    }
}