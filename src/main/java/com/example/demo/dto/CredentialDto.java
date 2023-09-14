package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class CredentialDto {
    private String username;
    private char[] password;

}
