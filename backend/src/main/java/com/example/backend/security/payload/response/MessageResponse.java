package com.example.backend.security.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {

    // Getter and Setter
    private String message;

    // No-args constructor
    public MessageResponse() {
    }

    // All-args constructor
    public MessageResponse(String message) {
        this.message = message;
    }

    // toString method
    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
