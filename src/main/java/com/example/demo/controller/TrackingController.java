package com.example.demo.controller;

import com.example.demo.service.TrackingService;
import com.example.demo.service.impl.TrackingServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * manage tracking related flow
 */
@RestController
@RequestMapping("api/tracking")
@Slf4j
@AllArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    /**
     * update tracking stage
     * @param state //forward or backward
     * @param orderId
     * @param token //specific user token for verify user
     * @return ResponseEntity<'ok'> or ResponseEntity<'error'>
     */
    @PostMapping("update")
    private ResponseEntity<String> updateTrackingStep(@RequestParam(name = "type") int state ,
                                                      @RequestParam(name = "id") String orderId ,
                                                      @RequestParam(name = "token") String token) {
        boolean st;
        st = state == 1;
        return trackingService.changeTrackingStage(orderId , token , st);
    }
}
