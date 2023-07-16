package com.example.demo.controller;

import com.example.demo.model.ListingFileUpload;
import com.example.demo.service.ListingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestPart("file") MultipartFile file)
            throws IOException {
        log.info("file received "+file.getName());
        return listingService.uploadBulkListing(file);
    }

    @GetMapping("files")
    private ResponseEntity<Page<ListingFileUpload>> getAllFiles(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size){
        return listingService.getUploadedFiles(page,size);
    }


}
