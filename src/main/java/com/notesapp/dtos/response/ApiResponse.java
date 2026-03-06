package com.notesapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final String timestamp;
    private final int    status;
    private final String message;
    private final T      data;


    private ApiResponse(int status, String message, T data) {
        this.timestamp = LocalDateTime.now()
                                      .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.status  = status;
        this.message = message;
        this.data    = data;
    }


    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }

    public static <T> ApiResponse<T> validationError(T data) {
        return new ApiResponse<>(400, "Validation failed", data);
    }
}
