package com.example.demo.model.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.SecureRandomParameters;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearch implements Serializable {
    private String name;

    private String description;

    private Integer quantityFrom;
    private Integer quantityTo;

    private Double priceFrom;
    private Double priceTo;

    private String firm;

}
