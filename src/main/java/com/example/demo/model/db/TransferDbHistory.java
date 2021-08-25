package com.example.demo.model.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer_db_history")
public class TransferDbHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String elasticId;

    private String from1;

    private String to1;

    private Double howMuch;

    private Integer score;
    private boolean removed;

    private Integer docBeenDeletedAndRecovered = new Integer(0);

    public void set(Integer docBeenDeletedAndRecovered) {
        this.docBeenDeletedAndRecovered = ++docBeenDeletedAndRecovered;
    }
}
