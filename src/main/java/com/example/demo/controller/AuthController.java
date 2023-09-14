package com.example.demo.controller;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.*;
import com.example.demo.model.SellerDetails;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

/**
 * authentication flow
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {

    private final SellerDetailsRepository sellerDetailsRepository;
    private final AuthService authService;
    private final UserAuthProvider userAuthProvider;

    /**
     * user login
     * @param loginRequest
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialDto loginRequest){
        log.info("Login request from :"+loginRequest);
        return authService.login(loginRequest);
    }

    /**
     * signup new user
     * @param signUpDto
     * @return ResponseEntity<UserDto>
     */

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        log.info("register request get" +signUpDto);
        return authService.register(signUpDto);

    }

    @DeleteMapping("{username}")
    private void delete(@PathVariable String username){
        authService.deleteUser(username);
    }

}
