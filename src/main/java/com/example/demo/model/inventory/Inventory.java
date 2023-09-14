package com.example.demo.model.inventory;


import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;
import java.time.Instant;
import java.util.List;

//@Entity
@Document("inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {


    @Id
    private String id;
    private String name;
    @DBRef
    private SubCategory subCategory;
    @DBRef
    private Brand brand;
    private String serialNumber;
    private String itemDescription;
    private String sellerUsername;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled = true;
    private Status status;
    private String imgUrl;
    private String dltUrl;
    @DBRef
    private List<Variant> variants;
    private int qty;
    private double lowestPrice;
    private String[] variantsList;
}
