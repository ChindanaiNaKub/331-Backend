package se331.lab.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jakarta.servlet.ServletException;

@Component
public class CloudStorageHelper {
    private static Storage storage = null;

    static {
        InputStream serviceAccount = null;
        try {
            StorageOptions.Builder builder = StorageOptions.newBuilder();

            // Prefer Application Default Credentials (workload identity, gcloud auth, etc.)
            String adcDisabled = System.getenv("DISABLE_ADC");
            boolean useAdc = !StringUtils.hasText(adcDisabled) || !adcDisabled.equalsIgnoreCase("true");

            if (useAdc) {
                try {
                    builder.setCredentials(GoogleCredentials.getApplicationDefault());
                    System.out.println("Using Application Default Credentials");
                } catch (IOException adcException) {
                    System.err.println("ADC not available: " + adcException.getMessage());
                    useAdc = false; // Fall back to explicit credentials
                }
            }

            // If ADC failed or is disabled, use explicit credentials
            if (!useAdc) {
                String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
                if (StringUtils.hasText(credentialsPath)) {
                    try {
                        serviceAccount = new java.io.FileInputStream(credentialsPath);
                        builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                        System.out.println("Using credentials from GOOGLE_APPLICATION_CREDENTIALS: " + credentialsPath);
                    } catch (IOException e) {
                        System.err.println("Failed to load credentials from GOOGLE_APPLICATION_CREDENTIALS: " + e.getMessage());
                        throw new RuntimeException("Failed to load credentials from GOOGLE_APPLICATION_CREDENTIALS: " + credentialsPath, e);
                    }
                } else {
                    // Last resort: attempt classpath resource
                    try {
                        serviceAccount = new ClassPathResource("imageuploadcompo-7a4757918eb5.json").getInputStream();
                        builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                        System.out.println("Using classpath credentials: imageuploadcompo-7a4757918eb5.json");
                    } catch (Exception e) {
                        System.err.println("Failed to load classpath credentials: " + e.getMessage());
                        throw new RuntimeException("No valid credentials found. Please set GOOGLE_APPLICATION_CREDENTIALS environment variable or ensure credentials file is in classpath.", e);
                    }
                }
            }

            // Optional explicit project id via env; otherwise let SDK infer
            String projectId = System.getenv("GCP_PROJECT_ID");
            if (StringUtils.hasText(projectId)) {
                builder.setProjectId(projectId);
                System.out.println("Using project ID from GCP_PROJECT_ID: " + projectId);
            }

            storage = builder.build().getService();
            System.out.println("CloudStorageHelper initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize CloudStorageHelper: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("CloudStorageHelper initialization failed", e);
        } finally {
            if (serviceAccount != null) {
                try {
                    serviceAccount.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public String uploadFile(MultipartFile filePart, final String bucketName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmssSSS");
        String dtString = sdf.format(new Date());
        final String fileName = dtString + "-" + filePart.getOriginalFilename();

        InputStream is = filePart.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] readBuf = new byte[4096];
        while (is.available() > 0) {
            int bytesRead = is.read(readBuf);
            os.write(readBuf, 0, bytesRead);
        }

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName)
                        .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                        .setContentType(filePart.getContentType())
                        .build(),
                os.toByteArray());
        return blobInfo.getMediaLink();
    }

    public String getImageUrl(MultipartFile file, final String bucket) throws IOException, ServletException {
        final String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
            for (String s : allowedExt) {
                if (extension.equals(s)) {
                    return this.uploadFile(file, bucket);
                }
            }
            throw new ServletException("file must be an image");
        }
        return null;
    }

    public StorageFileDto getStorageFileDto(MultipartFile file, final String bucket) throws IOException, ServletException {
        final String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
            for (String s : allowedExt) {
                if (extension.equals(s)) {
                    String urlName = this.uploadFile(file, bucket);
                    return StorageFileDto.builder()
                            .name(urlName)
                            .build();
                }
            }
            throw new ServletException("file must be an image");
        }
        return null;
    }
}


