package com.example.demo.exceptions.custom;

import com.example.demo.exceptions.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {
  private ErrorCode errorCode;

  public CustomRuntimeException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
