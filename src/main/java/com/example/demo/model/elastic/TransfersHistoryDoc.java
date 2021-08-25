package com.example.demo.model.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "transfer_history")
public class TransfersHistoryDoc implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String from;
    @Field(type = FieldType.Text)
    private String to;
    @Field(type = FieldType.Double)
    private Double howMuch;
}
