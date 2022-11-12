package com.habit.thehabit.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RefreshScope
@Component
public class AwsFileUploadUtils {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    public AwsFileUploadUtils(AmazonS3 amazonS3){
        this.amazonS3 = amazonS3;
    }

    public String fileUpload(MultipartFile imageFile, String directory) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(imageFile.getContentType());
        objectMetadata.setContentLength(imageFile.getInputStream().available());

        String fileName = directory + "/" + UUID.randomUUID().toString().replace("-", "");
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), objectMetadata));

        return amazonS3.getUrl(bucket, fileName).toString();
    }

}
