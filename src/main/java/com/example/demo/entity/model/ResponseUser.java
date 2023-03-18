package com.example.demo.entity.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ResponseUser {
    private boolean error;
    private String description;
}
