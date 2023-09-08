package com.example.demo.controller;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.BulkInputDto;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.Orders;
import com.example.demo.service.ListingService;
import com.example.demo.service.SessionUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/listing")
@AllArgsConstructor
@Slf4j
public class ListingController {

    private final ListingService listingService;
    private final SessionUser info;

    @PostMapping(value = "bulkUpload",
            consumes = MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> uploadBulkListing(
            @RequestPart("file") MultipartFile file) {
        log.info("bulkUpload, " +info.getCurrentUserUsername());
        return listingService.uploadBulkListing(file);
    }

    @GetMapping("files")
    private ResponseEntity<Page<ListingFileUpload>> getAllFiles(
            @RequestParam(name = "page") int page ,
            @RequestParam(name = "size") int size) {
        return listingService.getUploadedFiles(page , size);
    }

    @GetMapping("orders")
    private ResponseEntity<Page<Orders>> getAllOrders(
            @RequestParam(name = "page") int page ,
            @RequestParam(name = "size") int size) {
        return listingService.getListedOrders(page , size);
    }

    @PostMapping("singleInput")
    private ResponseEntity<String> singleInput(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token ,
            @RequestBody Orders orders) {
        return listingService.addSingleOrder(orders);
    }

    @PostMapping("createBulkUploadFile")
    private ResponseEntity<String> createBulkUploadFileExcelFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token ,
            @RequestBody List<Orders> list) {
        log.info("retrieved lis : " + list);
        return listingService.generateBulkUploadList(token , list);
    }

    @GetMapping("getBulkInputList")
    private ResponseEntity<List<BulkInputDto>> getBulkInputList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return listingService.getBulkInputList();
    }

    @GetMapping("searchByDate")
    private ResponseEntity<Page<Orders>> getOrdersByDate(
            @RequestParam("startDate") Instant startDate,
            @RequestParam("endDate") Instant endDate,
            @RequestParam("page") int page,
            @RequestParam("size") int size){
        log.info("start date - "+startDate+ " "+"end date - "+endDate);
        return listingService.getOrdersByDate(startDate, endDate, page, size);
    }

    @GetMapping("searchById")
    private ResponseEntity<Page<Orders>> getOrdersById(
            @RequestParam("orderId") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size){
        return listingService.getOrdersById(id , page, size);
    }

    @GetMapping("searchByContactNo")
    private ResponseEntity<Page<Orders>> getOrdersByContactNo(
            @RequestParam("contactNo") String contactNo,
            @RequestParam("page") int page,
            @RequestParam("size") int size){
        return listingService.getOrdersByContactNumber(contactNo , page, size);
    }
}
