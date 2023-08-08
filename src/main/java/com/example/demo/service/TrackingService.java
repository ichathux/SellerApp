package com.example.demo.service;

import com.example.demo.enums.Status;
import com.example.demo.model.Orders;
import com.example.demo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class TrackingService {

    private final OrderRepository orderRepository;

    public ResponseEntity<String> changeTrackingStage(Orders orders){
        try{
            Status currentStatus = orders.getStatus();
            switch (currentStatus){
                case ORDER_PLACED:
                    currentStatus = Status.PACKED;
                    break;

                case PACKED:
                    currentStatus = Status.COLLECTED_DELIVERY_PARTNER;
                    break;

                case COLLECTED_DELIVERY_PARTNER:
                    break;

                default:
                    break;

            }
            orders.setStatus(currentStatus);
            orders.setUpdatedAt(Instant.now());
            orderRepository.save(orders);

            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.error("error occurred "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }
}
