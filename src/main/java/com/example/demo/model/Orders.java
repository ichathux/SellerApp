package com.example.demo.model;

import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderDescription;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "contactNo")
    private Customer customer;
    private Double price;
    private Double deliveryCharge;
    private String remarks;
//    @ManyToOne
//    @JoinColumn(name = "seller_id", referencedColumnName = "contactNo")
//    private SellerDetails sellerDetails;
    private String sellerUsername;
    private Status status;
    private Instant createdAt;
    private Instant packedAt;
    private Instant collectedAt;
    private Instant deliveredAt;
    private Instant updatedAt;
}
