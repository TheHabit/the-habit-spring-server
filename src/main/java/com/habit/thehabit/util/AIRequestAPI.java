package com.habit.thehabit.util;

import com.habit.thehabit.friend.app.dto.AIRequestDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name="AIRequestAPI", url = "192.168.0.28:5000")
public interface AIRequestAPI {
    @PostMapping(value = "/recommend", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Headers("Content-Type:multipart/form-data")
    List<Integer> callRecommanedFriends(AIRequestDTO aiRequestDTO);
}
