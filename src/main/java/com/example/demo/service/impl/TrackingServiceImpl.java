package com.example.demo.service.impl;

import com.example.demo.enums.Status;
import com.example.demo.model.Orders;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TrackingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TrackingServiceImpl implements TrackingService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private boolean checkOrderIdIsValid(String orderID , String token) {
        Optional<Orders> order = orderRepository.findById(orderID);
        if (order.isPresent()) {
            Optional<User> user = userRepository.findByUsername(order.get().getSellerUsername());
            return user.filter(value -> token.equals(value.getRequestToken())).isPresent();
        } else return false;
    }

    @Override
    public ResponseEntity<String> changeTrackingStage(String orderId ,
                                                      String token ,
                                                      boolean state) {

        Optional<Orders> order = orderRepository.findById(orderId);
        log.info("order ID - " + orderId + " changed state " + state);
        if (order.isEmpty()) {
            return new ResponseEntity<>("Order id not found" , HttpStatus.NOT_FOUND);
        }
        if (!checkOrderIdIsValid(orderId , token)) {
            return new ResponseEntity<>("Order id and token not match" , HttpStatus.CONFLICT);
        }
        try {
            Status currentStatus = order.get().getStatus();
            Status changedStatus = currentStatus;
            log.info("Order id - " + orderId + " current status - " + currentStatus);
            switch (currentStatus) {
                case ORDER_PLACED:
                    if (state) {
                        changedStatus = Status.PACKED;
                        order.get().setPackedAt(Instant.now());
                    }
                    break;

                case PACKED:
                    if (state) {
                        changedStatus = Status.COLLECTED_DELIVERY_PARTNER;
                        order.get().setCollectedAt(Instant.now());

                    } else {
                        changedStatus = Status.ORDER_PLACED;
                    }
                    break;

                case COLLECTED_DELIVERY_PARTNER:
                    if (state) {
                        changedStatus = Status.COMPLETE;
                        order.get().setDeliveredAt(Instant.now());
                    } else {
                        changedStatus = Status.PACKED;
                    }
                    break;

                case COMPLETE:
                    if (!state)
                        changedStatus = Status.COLLECTED_DELIVERY_PARTNER;
                    break;

                default:
                    break;

            }
            log.info("Order id - " + orderId + " changed status - " + currentStatus);
            order.get().setStatus(changedStatus);
            order.get().setUpdatedAt(Instant.now());
            orderRepository.save(order.get());

            return new ResponseEntity<>("Updated" , HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error occurred " + e.getMessage());
            return new ResponseEntity<>(e.getMessage() , HttpStatus.EXPECTATION_FAILED);
        }

    }
}
