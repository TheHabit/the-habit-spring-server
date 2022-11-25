package com.habit.thehabit.util;

import com.habit.thehabit.friend.app.dto.AIRequestDTO;
import com.habit.thehabit.friend.app.dto.UpdateDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@RefreshScope
@FeignClient(name="AIRequestAPI", url ="${ai.datasource.url}")
public interface AIRequestAPI {


    @PostMapping(value = "/recommend", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Headers("Content-Type:multipart/form-data")
    List<Integer> callRecommanedFriends(AIRequestDTO aiRequestDTO);

    @PostMapping(value ="addrecord", consumes = MediaType.APPLICATION_JSON_VALUE)
    Object sendDataToAi(List<UpdateDataDTO> updateDataDTOList);
}
