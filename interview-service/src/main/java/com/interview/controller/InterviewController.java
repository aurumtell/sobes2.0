package com.interview.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.interview.model.request.AnswerRequest;
import com.interview.model.request.DialogRequest;
import com.interview.model.request.MessageRequest;
import com.interview.model.response.AnswerResponse;
import com.interview.model.response.GenerateResponse;
import com.interview.model.response.QuestionResponse;
import com.interview.security.services.UserDetailsImpl;
import com.interview.service.QuestionService;
import com.interview.service.YandexGPTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@SecurityRequirement(name = "yandexGPT")
@Tag(description = "Api to send queries to GPT",
        name = "YandexGPT Resource")
public class InterviewController {
    @Autowired
    YandexGPTService yandexGPTService;

    @Autowired
    QuestionService questionService;

    Logger logger = LoggerFactory.getLogger(InterviewController.class);

    @PostMapping("/user/interview/generate")
    @ResponseBody
    public List<GenerateResponse> generateQuestions(@RequestBody MessageRequest message) throws JsonProcessingException {
        return yandexGPTService.generateQuestions(String.join(" ", message.getProfession(), message.getLevel()));
    }

    @Operation(summary = "Get questions",
            description = "Get questions by Profession")
    @PostMapping(value = "/user/interview/question")
    @ResponseBody
    public List<QuestionResponse> getQuestionsByProfession(@RequestBody MessageRequest message, @AuthenticationPrincipal UserDetailsImpl user){
        return questionService.getQuestionsByProfession(message.getProfession(), user);
    }

    @PostMapping("/user/interview/answer")
    @ResponseBody
    public AnswerResponse sendMessage(@RequestBody AnswerRequest answer, @AuthenticationPrincipal UserDetailsImpl user) throws IOException {
        return yandexGPTService.sendMessage(answer, user);
    }

    @Operation(summary = "Get dialog",
            description = "Get dialog by question")
    @PostMapping(value = "/user/interview/dialog")
    @ResponseBody
    public List<AnswerResponse> getDialogByQuestion(@RequestBody DialogRequest dialog, @AuthenticationPrincipal UserDetailsImpl user){
        return questionService.getDialogByQuestion(dialog.getQuestion(), user);
    }
}