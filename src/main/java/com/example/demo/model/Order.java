//package com.example.demo.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String orderDescription;
//    @ManyToOne
//    @JoinColumn(name = "customerId", referencedColumnName = "customerId")
//    private Customer customer;
//    private Double price;
//    private Double deliveryCharge;
//    private String remarks;
//    @ManyToOne
//    @JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
//    private SellerDetails sellerDetails;
//}
