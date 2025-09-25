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
                builder.setCredentials(GoogleCredentials.getApplicationDefault());
            } else {
                // Fallback to explicit credentials via env path
                String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
                if (StringUtils.hasText(credentialsPath)) {
                    serviceAccount = new java.io.FileInputStream(credentialsPath);
                    builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                } else {
                    // Last resort: attempt classpath resource if provided externally (not in repo)
                    try {
                        serviceAccount = new ClassPathResource("imageuploadcompo-7a4757918eb5.json").getInputStream();
                        builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                    } catch (Exception ignored) {
                        // no-op; will rely on ADC if available
                        builder.setCredentials(GoogleCredentials.getApplicationDefault());
                    }
                }
            }

            // Optional explicit project id via env; otherwise let SDK infer
            String projectId = System.getenv("GCP_PROJECT_ID");
            if (StringUtils.hasText(projectId)) {
                builder.setProjectId(projectId);
            }

            storage = builder.build().getService();
        } catch (IOException e) {
            e.printStackTrace();
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
}


