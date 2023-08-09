package com.example.demo.controller;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.SellerProfile;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
@Slf4j
public class AccountController {
    private final UserRepository userRepository;

    private final AccountService accountService;
    private final UserAuthProvider userAuthProvider;
    @PostMapping("completeProfile")
    public ResponseEntity<String> completeAccount(@RequestBody SellerProfile sellerProfile){
        log.info("completing user account  : "+sellerProfile);
        return new ResponseEntity<>("complete", HttpStatus.OK);
    }

    @GetMapping("getUserDetails")
    private ResponseEntity<SellerProfile> getDetails(){
        return accountService.getUserProfile();
    }

}
