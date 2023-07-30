package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String username;
    private char[] password;
    private String contact;
    private String businessName;
    private String address;


}
