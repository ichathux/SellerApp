package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
//@Entity
@Document("user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String userId;
    private String username;
    private String password;
    private Instant created;
    private boolean enabled;
    private String requestToken;
}
