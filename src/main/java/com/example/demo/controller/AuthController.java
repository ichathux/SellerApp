package com.example.demo.controller;

import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest){
//        log.info("sign up request received : "+registerRequest);

        if (authService.checkUserAlreadyExist(registerRequest.getEmail())){
            return new ResponseEntity<>("User already exist", HttpStatus.ALREADY_REPORTED);
        }else{
            authService.signUp(registerRequest);
            return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
        }

    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token){
        log.info("starting to verify account : "+token);
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        log.info("Login request from :"+loginRequest);
        return authService.login(loginRequest);
    }
    @PostMapping("refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
    }
}
