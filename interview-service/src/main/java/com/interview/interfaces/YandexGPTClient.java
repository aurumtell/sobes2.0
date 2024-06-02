package com.interview.interfaces;


import com.interview.model.request.ChatRequest;
import com.interview.model.response.ChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "yandexgpt", url = "https://llm.api.cloud.yandex.net/foundationModels/v1")
public interface YandexGPTClient {
    
    @PostMapping("/completion")
    ChatResponse generateText(@RequestHeader("Authorization") String bearerToken, @RequestBody ChatRequest chatRequest);
}

