package com.example.demo.model.inventory;


import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

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
    //    private int quantity;
//    private double unitPrice;
    private String itemDescription;
    private String fileName;
    private String fileLocation;
    private String sellerUsername;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean enabled = true;
    private Status status;
    private Long customField1 = 0L;
    private Double customField1Price = 0D;
    private Long customField2 = 0L;
    private Double customField2Price = 0D;
    private Long customField3 = 0L;
    private Double customField3Price = 0D;
    private Long customField4 = 0L;
    private Double customField4Price = 0D;
    private Long customField5 = 0L;
    private Double customField5Price = 0D;
    private Long customField6 = 0L;
    private Double customField6Price = 0D;
    @ManyToOne
    private CustomFieldData customFieldData;
    private String imgUrl;
}
