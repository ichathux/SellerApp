package com.example.demo.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//@Entity
@Document("brand")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Indexed(unique = true)
    private String name;

}
