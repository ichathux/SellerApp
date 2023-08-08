package com.example.demo.controller;

import com.example.demo.model.Orders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
@Slf4j
public class OrderController {

    @PostMapping("addSingleOrder")
    public ResponseEntity<String> addSingleOrder(){
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    @PostMapping("addBulkOrder")
    public ResponseEntity<String> addBulkOrders(){
        return new ResponseEntity<>("done", HttpStatus.OK);
    }


}
