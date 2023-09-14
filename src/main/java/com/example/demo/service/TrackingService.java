package com.example.demo.service;

import org.springframework.http.ResponseEntity;

public interface TrackingService {
    ResponseEntity<String> changeTrackingStage(String orderId ,
                                               String token ,
                                               boolean state);

}
