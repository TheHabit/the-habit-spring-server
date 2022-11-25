package com.habit.thehabit.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@Component
public class ReviewToOneLineUtils {

    private static final RestTemplate REST_TEMPLATE;
    @Value("${ai.datasource.url}")
    private String url;

    static {
        // RestTemplate 기본 설정을 위한 Factory 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);
        factory.setBufferRequestBody(false);
        REST_TEMPLATE = new RestTemplate(factory);
    }


    public String abStractOneLine(String review){

        /** ------------- 인공지능 API 서버로 review 전송하여 oneLineReview 얻기 */
        /** body 설정 */
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("review", review);

        /** header 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        /** http 통신할 entity 설정 */
        HttpEntity<?> requestEntity = new HttpEntity<>(body,headers);

        /** AI 서버 통신 후 한줄 요약(oneLineReview) 가져오기 */
        System.out.println("url = " + url);
        JsonNode response = REST_TEMPLATE.postForObject(url+"summary", requestEntity, JsonNode.class);
        System.out.println("response = " + response);

        String oneLineReview = null;

        if(response != null){
            oneLineReview = String.valueOf(response.get("result"));
        }

        return oneLineReview;
    }
}
