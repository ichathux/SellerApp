package com.example.demo.controller;

import com.example.demo.dto.SellerProfile;
import com.example.demo.model.SellerDetails;
import com.example.demo.service.AccountService;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/account")
@AllArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;
    @PostMapping("completeProfile")
    public ResponseEntity<String> completeAccount(@RequestBody SellerProfile sellerProfile){
        log.info("completing user account  : "+sellerProfile);
        accountService.completeAccount(sellerProfile);
        return new ResponseEntity<>("complete", HttpStatus.OK);
    }

    @GetMapping("getDetails")
    private void getDetails(){
        log.info("getting details "+authService.getCurrentUser().getUsername());
//        return new ResponseEntity<>(authService.getCurrentUser().getUsername() , HttpStatus.OK);
    }

    @GetMapping("getSellerProfile")
    private ResponseEntity<SellerDetails> getSellerProfile(){
        return new ResponseEntity<>(accountService.getUserProfile(), HttpStatus.OK);
    }
}
