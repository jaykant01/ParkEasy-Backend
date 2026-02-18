package com.jay.ParkEasy_Backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    public List<String> uploadImages(MultipartFile[] images) {

        if (images == null || images.length == 0) {
            throw new RuntimeException("At least one image is required.");
        }

        List<String> keys = new ArrayList<>();

        for (MultipartFile image : images) {

            if (image.isEmpty()) {
                throw new RuntimeException("Uploaded image is empty.");
            }

            if (!image.getContentType().startsWith("image")) {
                throw new RuntimeException("Only image files allowed.");
            }

            if (image.getSize() > 5_000_000) {
                throw new RuntimeException("Max file size is 5MB.");
            }

            String key =
                    "parking-spaces/" +
                            UUID.randomUUID() +
                            "-" +
                            image.getOriginalFilename();

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(image.getContentType())
                            .build();

            try {

                System.out.println("Uploading to S3: " + key);

                s3Client.putObject(
                        request,
                        software.amazon.awssdk.core.sync.RequestBody
                                .fromBytes(image.getBytes())
                );

                System.out.println("✅ SUCCESS");

            } catch (Exception e) {

                e.printStackTrace();

                throw new RuntimeException("S3 upload failed.");
            }

            keys.add(key);
        }

        return keys;
    }



    public String getImageUrl(String key) {

        return "https://" + bucketName +
                ".s3." + region +
                ".amazonaws.com/" + key;
    }
}
