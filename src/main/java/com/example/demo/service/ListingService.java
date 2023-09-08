package com.example.demo.service;

import com.example.demo.dto.BulkInputDto;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public interface ListingService {
    ResponseEntity<String> uploadBulkListing(MultipartFile file);
    ResponseEntity<Page<ListingFileUpload>> getUploadedFiles(int page ,
                                                             int size);
    ResponseEntity<Page<Orders>> getListedOrders(int page ,
                                                 int size);
    ResponseEntity<String> addSingleOrder(Orders orders);
    ResponseEntity<String> generateBulkUploadList(String token ,
                                                  List<Orders> list);
    ResponseEntity<List<BulkInputDto>> getBulkInputList();
    ResponseEntity<Page<Orders>> getOrdersByDate(Instant startDate ,
                                                 Instant endDate ,
                                                 int page ,
                                                 int size);
    ResponseEntity<Page<Orders>> getOrdersById(Long id ,
                                               int page ,
                                               int size);
    ResponseEntity<Page<Orders>> getOrdersByContactNumber(String contactNo ,
                                                          int page ,
                                                          int size);
}
