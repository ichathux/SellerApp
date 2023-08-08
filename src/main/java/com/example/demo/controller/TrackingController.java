package com.example.demo.controller;

import com.example.demo.service.TrackingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tracking")
@Slf4j
@AllArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("update")
    private ResponseEntity<String> updateTrackingStep(@RequestParam(name = "type") int state ,
                                                      @RequestParam(name = "id") long orderId ,
                                                      @RequestParam(name = "token") String token) {

        boolean st;
        st = state == 1;

        return trackingService.changeTrackingStage(orderId , token , st);
    }
}
