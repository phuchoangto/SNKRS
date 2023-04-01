package com.example.snkrs.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse implements Serializable {
    private final String message;
    private final Object data;
}
