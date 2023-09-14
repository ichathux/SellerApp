package com.example.demo.model.inventory;

import com.example.demo.model.inventory.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;

//@Entity
@Document("subCategory")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategory {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
//    private Long category;
//    @ManyToOne
//    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @DBRef
    private Category  category;

    public SubCategory(String name , Category category) {
        this.name = name;
        this.category = category;
    }
}
