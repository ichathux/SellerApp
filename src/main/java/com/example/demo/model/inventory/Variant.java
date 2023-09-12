package com.example.demo.model.inventory;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String variants;
    private int qty;
    private double price;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Inventory item;
    private String imgUrl;
    private String publicID;
    private boolean isDisable = false;
    private boolean isOutOfStock = false;
}
