package com.example.demo.config.security;

import com.example.demo.common.ApiResponse;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    response.setStatus(errorCode.getStatusCode().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ApiResponse<?> apiResponse = ApiResponse.builder().message(errorCode.getMessage()).build();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
      response.flushBuffer();
    } catch (IOException e) {
      throw new CustomRuntimeException(ErrorCode.UNAUTHENTICATED);
    }
  }
}
