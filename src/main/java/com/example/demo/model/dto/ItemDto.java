package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.security.SecureRandomParameters;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {

    private String name;

    private String description;

    private Integer quantity;

    private Double price;

    private String firm;

}
