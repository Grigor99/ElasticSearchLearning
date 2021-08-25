package com.example.demo.model.db;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ItemDbEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    private String elasticId;

    private Integer score;

    private String name;

    private String description;

    private Integer quantity;

    private Double price;

    private String firm;

    private boolean removed;

    public ItemDbEntity(String elasticId, Integer score, String name, String description, Integer quantity, Double price, String firm) {
        this.elasticId = elasticId;
        this.score = score;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.firm = firm;
    }
}
