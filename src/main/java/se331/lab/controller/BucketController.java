package se331.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import se331.lab.util.CloudStorageHelper;
import se331.lab.util.StorageFileDto;

import java.io.IOException;
import jakarta.servlet.ServletException;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BucketController {
    final CloudStorageHelper cloudStorageHelper;
    @org.springframework.beans.factory.annotation.Value("${application.gcs.bucket}")
    String bucketName;

    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException, ServletException {
        return ResponseEntity.ok(this.cloudStorageHelper.getImageUrl(file, bucketName));
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadFileComponent(@RequestPart(value = "image") MultipartFile file) throws IOException, ServletException {
        StorageFileDto dto = this.cloudStorageHelper.getStorageFileDto(file, bucketName);
        return ResponseEntity.ok(dto);
    }
}
