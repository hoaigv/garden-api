package com.example.demo.files.impl;



import com.example.demo.common.CloudUtils;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.files.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileServiceImpl implements IFileService {

  CloudUtils cloudUtils;

  @Override
  public String uploadFile(MultipartFile file) {
    System.out.println(file.getOriginalFilename());
    String fileName = file.getOriginalFilename();
    if (!Objects.requireNonNull(fileName).endsWith(".JPG")
        && !fileName.endsWith(".png")
        && !fileName.endsWith(".tiff")
        && !fileName.endsWith(".webp")
        && !fileName.endsWith(".jfif")) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_SUPPORTED);
    }

    CompletableFuture<String> uploadFuture = cloudUtils.uploadFileAsync(file);

    try {
      return uploadFuture.get();
    } catch (Exception e) {
      throw new CustomRuntimeException(ErrorCode.SET_IMAGE_FAILED);
    }
  }
}
