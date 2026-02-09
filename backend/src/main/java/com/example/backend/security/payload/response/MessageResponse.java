package com.example.backend.security.payload.response;

public class MessageResponse {

    private String message;

    // No-args constructor
    public MessageResponse() {
    }

    // All-args constructor
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
