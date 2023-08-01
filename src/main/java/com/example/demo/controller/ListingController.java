package com.example.demo.controller;

import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.Orders;
import com.example.demo.service.ListingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/listing")
@AllArgsConstructor
@Slf4j
public class ListingController {

    private final ListingService listingService;

    @PostMapping(value = "bulkUpload",
            consumes = MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> uploadBulkListing(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token ,
            @RequestPart("file") MultipartFile file) {
        log.info("file received " + token);
        return listingService.uploadBulkListing(file , token);
    }

    @GetMapping("files")
    private ResponseEntity<Page<ListingFileUpload>> getAllFiles(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token ,
            @RequestParam(name = "page") int page ,
            @RequestParam(name = "size") int size) {
        return listingService.getUploadedFiles(page , size , token);
    }

    @GetMapping("orders")
    private ResponseEntity<Page<Orders>> getAllOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token ,
            @RequestParam(name = "page") int page ,
            @RequestParam(name = "size") int size) {

        return listingService.getListedOrders(page, size, token);
    }


}
