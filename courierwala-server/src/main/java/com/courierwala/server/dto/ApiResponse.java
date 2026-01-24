package com.courierwala.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResponse {
    private LocalDateTime timeStamp;
    private String message;
    private String status;

    public ApiResponse(String message, String status) {
        super();
        this.message = message;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }

}
