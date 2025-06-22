package com.example.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED("Unauthorized access !", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(" Please log in !", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_FAILED("Refresh token failed.", HttpStatus.BAD_REQUEST),
    TOKEN_CREATION_FAILED("Token creation failed: bad request", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("User already exists.", HttpStatus.CONFLICT),

    PASSWORD_INCORRECT("Password is incorrect.", HttpStatus.UNAUTHORIZED),

    IMAGE_UPLOAD_FAILED("Image upload failed.", HttpStatus.BAD_REQUEST),
    IMAGE_DELETE_FAILED("Image deletion failed.", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND("Requested image was not found.", HttpStatus.NOT_FOUND),
    IMAGE_NOT_SUPPORTED("Image format is not supported.", HttpStatus.BAD_REQUEST),
    SET_IMAGE_FAILED("Setting image failed.", HttpStatus.INTERNAL_SERVER_ERROR),

    // Garden cell service errors
    RESOURCE_NOT_FOUND("Requested resource was not found.", HttpStatus.NOT_FOUND),
    GARDEN_NOT_FOUND("Garden was not found.", HttpStatus.NOT_FOUND),
    INVALID_ARGUMENT("Invalid argument.", HttpStatus.BAD_REQUEST),

    // Inventory
    INSUFFICIENT_QUANTITY("Insufficient quantity seedling ! ", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus statusCode;


    public int getCode() {
        return statusCode.value();
    }

    ErrorCode(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
