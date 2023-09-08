package com.example.demo.model.inventory;


import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id")
    private SubCategory subCategory;
    @ManyToOne
    @JoinColumn(name = "Brand_id", referencedColumnName = "id")
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
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Variant> variants;
    private int qty;
    private double lowestPrice;
    private String[] variantsList;
}
