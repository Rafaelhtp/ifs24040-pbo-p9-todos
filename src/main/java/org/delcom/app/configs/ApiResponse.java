package org.delcom.app.configs;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    // Konstruktor lengkap
    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Konstruktor 2-argumen dihapus

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}