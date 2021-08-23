package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "blog")
public class Article {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String unit;
    @Field(type = FieldType.Text)
    private String ages;
    @Field(type = FieldType.Double)
    private Double salary;

}