package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
//@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("commets")
public class Comment {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
//    @NotEmpty
    private String text;
    private Instant createdDate;
//    @ManyToOne
//    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @DBRef
    private User user;

}
