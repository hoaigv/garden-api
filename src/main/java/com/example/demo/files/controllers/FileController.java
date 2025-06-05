package com.example.demo.files.controllers;


import com.example.demo.common.ApiResponse;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.files.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileController {
  IFileService fileService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateImage(@RequestParam MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_FOUND);
    }
    var imageUrl = fileService.uploadFile(file);
    var resp = ApiResponse.builder().result(imageUrl).message("Update Image successfully").build();
    return ResponseEntity.ok(resp);
  }
}
