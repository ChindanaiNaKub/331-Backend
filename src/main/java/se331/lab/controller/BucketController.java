package se331.lab.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import se331.lab.util.CloudStorageHelper;
import se331.lab.util.StorageFileDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BucketController {
    final CloudStorageHelper cloudStorageHelper;
    @org.springframework.beans.factory.annotation.Value("${application.gcs.bucket}")
    String bucketName;

    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException, ServletException {
        try {
            log.info("Received file upload request: {}", file.getOriginalFilename());
            log.info("File size: {} bytes", file.getSize());
            log.info("Content type: {}", file.getContentType());

            String result = this.cloudStorageHelper.getImageUrl(file, bucketName);
            log.info("Upload successful for file: {}", file.getOriginalFilename());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error uploading file: {}", file.getOriginalFilename(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("file", file.getOriginalFilename());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadFileComponent(@RequestPart(value = "image") MultipartFile file) throws IOException, ServletException {
        try {
            log.info("Received image upload request: {}", file.getOriginalFilename());
            log.info("File size: {} bytes", file.getSize());
            log.info("Content type: {}", file.getContentType());

            StorageFileDto dto = this.cloudStorageHelper.getStorageFileDto(file, bucketName);
            log.info("Upload successful for image: {}", file.getOriginalFilename());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error uploading image: {}", file.getOriginalFilename(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("file", file.getOriginalFilename());
            return ResponseEntity.status(500).body(error);
        }
    }
}
