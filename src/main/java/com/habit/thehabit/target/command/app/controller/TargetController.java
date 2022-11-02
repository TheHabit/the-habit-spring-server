package com.habit.thehabit.target.command.app.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.habit.thehabit.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/target")
public class TargetController {

    private static final RestTemplate REST_TEMPLATE;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    static {
        // RestTemplate 기본 설정을 위한 Factory 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        factory.setBufferRequestBody(false);
        REST_TEMPLATE = new RestTemplate(factory);
    }

    @PostMapping("/receive")
    public void test(@RequestBody List<MultipartFile> files, ServletRequest request) throws IOException {
        MultipartFile file = files.get(0);
        System.out.println("file.getBytes().toString() = " + file.getBytes().toString());

        String imageName = "test";

        String replaceFileName = null;

        try {
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, file);

        } catch (IOException e) {
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }

    }

    @PostMapping("")
    public ResponseEntity<?> verifyTarget( MultipartFile file) throws IOException {

        System.out.println("file = " + file);
        System.out.println("image.getName() = " + file.getName());

        String fileName = file.getOriginalFilename();
        System.out.println("fileName = " + fileName);
        System.out.println("file.getBytes() = " + file.getBytes());

        /** 이미지 파일 로컬 저장 */
        String imageName = "test3";

        String replaceFileName = null;

        try {
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, file);

        } catch (IOException e) {
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }

        /** 인공지능 API 서버로 전송 */
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());
        body.add("num", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<?> requestEntity = new HttpEntity<>(body,headers);

        String url = "http://172.17.134.25:5005/detection";
        JsonNode response = REST_TEMPLATE.postForObject(url,requestEntity, JsonNode.class);
        System.out.println("response = " + response);

        if(response.get("result").asInt() == 1){
            return new ResponseEntity<>( true, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
//        return new ResponseEntity<>( response, HttpStatus.OK );
    }

}

