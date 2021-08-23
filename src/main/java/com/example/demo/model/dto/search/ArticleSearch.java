package com.example.demo.model.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearch implements Serializable {
    private String keyword;
    private String unit;
    private Double salary;

}
