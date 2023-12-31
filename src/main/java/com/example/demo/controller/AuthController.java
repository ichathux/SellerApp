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
        ResponseEntity<UserDto> userDto = authService.login(loginRequest);
        String token = userAuthProvider.createToken(userDto.getBody());
        userDto.getBody().setToken(token);
        String reqToken = authService.getRequestTokenForUser(userDto.getBody().getUsername());
        Optional<SellerDetails> sellerDetails = sellerDetailsRepository.findByUsername(loginRequest.getUsername());
        if (sellerDetails.isPresent()){
            userDto.getBody().setLogo(sellerDetails.get().getLogo());
            userDto.getBody().setBusinessName(sellerDetails.get().getDisplayName());
        }
        userDto.getBody().setRequestToken(reqToken);
        return new ResponseEntity<>(userDto.getBody(),HttpStatus.OK);
    }

    /**
     * signup new user
     * @param signUpDto
     * @return ResponseEntity<UserDto>
     */

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        log.info("register request get" +signUpDto);
        UserDto createdUser = authService.register(signUpDto);
        String token = userAuthProvider.createToken(createdUser);
        createdUser.setToken(token);
        authService.setRequestTokenForUser(token,createdUser.getUsername());
        createdUser.setRequestToken(token);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId ())).body(createdUser);

    }

}
