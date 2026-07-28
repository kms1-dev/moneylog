package org.example.moneylog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final String code;
    private final T data;
    private final ResponseMeta meta;

    private ApiResponse(boolean success, String message, String code, T data, ResponseMeta meta) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
        this.meta = meta;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, null, data, null);
    }

    public static <T> ApiResponse<T> successWithMeta(String message, T data, ResponseMeta meta) {
        return new ApiResponse<>(true, message, null, data, meta);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, message, code, null, null);
    }
}
