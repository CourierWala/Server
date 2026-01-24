package com.courierwala.server.exception;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String path;
    private int status;

    public ErrorResponse(String message, String path, int status) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.path = path;
        this.status = status;
    }

    // getters & setters
}

