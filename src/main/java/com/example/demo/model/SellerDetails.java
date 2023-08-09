package com.example.demo.model;

import com.example.demo.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDetails {

    @Id
    private Long contactNo;
    private String username;
    private String ownerName;
    private String displayName;
    private String address;
    private String logo;
    private String location;
    private boolean isCompleted = false;
}
