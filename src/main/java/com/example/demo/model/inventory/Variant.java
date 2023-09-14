package com.example.demo.model.inventory;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("variant")
@Data
public class Variant {

    @Id
    private String id;
    private String variants;
    private int qty;
    private double price;
//    @DBRef
//    private Inventory item;
    private String imgUrl;
    private String publicID;
    private boolean isDisable = false;
    private boolean isOutOfStock = false;
}
