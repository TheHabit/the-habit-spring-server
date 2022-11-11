package com.habit.thehabit.target.command.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
public class UploadToS3 {

    private static final RestTemplate REST_TEMPLATE;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    static {
        // RestTemplate 기본 설정을 위한 Factory 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        factory.setBufferRequestBody(false);
        REST_TEMPLATE = new RestTemplate(factory);
    }

    public UploadToS3(AmazonS3 amazonS3, TargetService targetService){
        this.amazonS3 = amazonS3;
    }

    /*S3에 이미지 파일 업로드를 위한 메소드*/
    public String upload(MultipartFile file, String dir) throws IOException {

        System.out.println("file = " + file);
        System.out.println("image.getName() = " + file.getName());

        String fileName = file.getOriginalFilename();
        System.out.println("fileName = " + fileName);
        System.out.println("file.getBytes() = " + file.getBytes());

        return fileUpload(file, dir);
    }

    @Transactional
    public String fileUpload(MultipartFile imageFile , String dir) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageFile.getInputStream().available());
        objectMetadata.setContentType(imageFile.getContentType());

        String fileName = dir + UUID.randomUUID().toString().replace("-", "");
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), objectMetadata));

        return amazonS3.getUrl(bucket, fileName).toString();
    }

            /** 이미지 파일 로컬 저장 */
//        String imageName = "test3";
//
//        String replaceFileName = null;
//
//        try {
//            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, file);
//
//        } catch (IOException e) {
//            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
//            throw new RuntimeException(e);
//        }


//        /** 인공지능 API 서버로 전송 */
//        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("image", file.getResource());
//        body.add("num", 1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<?> requestEntity = new HttpEntity<>(body,headers);
//
//        String url = "http://172.17.134.25:5005/detection";
//        JsonNode response = REST_TEMPLATE.postForObject(url,requestEntity, JsonNode.class);
//        System.out.println("response = " + response);
//
//        if(response.get("result").asInt() == 1){
//            return new ResponseEntity<>( true, HttpStatus.OK);
//        } else{
//            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
//        }
//        return new ResponseEntity<>( response, HttpStatus.OK );
    }



