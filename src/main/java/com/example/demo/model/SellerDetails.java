package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;

//@Entity
@Document("sellerDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDetails {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;
    private Long contactNo;
    private String username;
    private String ownerName;
    private String displayName;
    private String address;
    private String logo = "https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp";
    private String logoPublicId;
    private String logoPublicIdOld;
    private String location;
    private boolean isCompleted = false;
    private boolean inventory = false;
    private String country;
    private String city;
    private String description;
    private String nic;
}
