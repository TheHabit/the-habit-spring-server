package com.habit.thehabit.target.command.app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
public class TargetService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public TargetService(AmazonS3 amazonS3){
        this.amazonS3 = amazonS3;
    }

    @Transactional
    public String fileUpload(MultipartFile imageFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageFile.getInputStream().available());
        objectMetadata.setContentType(imageFile.getContentType());

        String fileName = "record/" + UUID.randomUUID().toString().replace("-", "");
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), objectMetadata));

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
