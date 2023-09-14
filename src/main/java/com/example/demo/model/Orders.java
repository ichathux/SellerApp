package com.example.demo.model;

import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;
import java.time.Instant;


//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("orders")
public class Orders {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String orderDescription;
//    @ManyToOne
//    @JoinColumn(name = "customer_id", referencedColumnName = "contactNo")
    @DBRef
    private Customer customer;
    private Double price;
    private Double deliveryCharge;
    private String remarks;
    private String sellerUsername;
    private Status status;
    private Instant createdAt;
    private Instant packedAt;
    private Instant collectedAt;
    private Instant deliveredAt;
    private Instant updatedAt;
}
