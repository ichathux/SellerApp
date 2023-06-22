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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private String displayName;
    private String address;
    private String ContactNo;
    private String logo;
    private String location;
    private boolean isCompleted = false;
    private BusinessType businessType;
}
