package com.example.demo.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
@Document("category")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
