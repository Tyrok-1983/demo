package com.example.demo.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class TestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer fields;

    public Integer getFields() {
        return fields;
    }

    public void setFields(Integer fields) {
        this.fields = fields;
    }
}
