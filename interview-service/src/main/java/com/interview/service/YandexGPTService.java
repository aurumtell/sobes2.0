package com.interview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.interview.exception.BusinessException;
import com.interview.exception.MyEntityNotFoundException;
import com.interview.interfaces.YandexGPTClient;
import com.interview.model.entity.AnswerEntity;
import com.interview.model.entity.ProfessionEntity;
import com.interview.model.entity.QuestionEntity;
import com.interview.model.entity.UserEntity;
import com.interview.model.request.AnswerRequest;
import com.interview.model.request.ChatRequest;
import com.interview.model.response.AnswerResponse;
import com.interview.model.response.ChatResponse;
import com.interview.model.response.GenerateResponse;
import com.interview.repo.*;
import com.interview.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class YandexGPTService {

    private final YandexGPTClient yandexGPTClient;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfessionRepository professionRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    public YandexGPTService(YandexGPTClient yandexGPTClient) {
        this.yandexGPTClient = yandexGPTClient;
    }


    public List<GenerateResponse> generateQuestions(String message) throws JsonProcessingException {
        ChatRequest.CompletionOptions completionOptions = new ChatRequest.CompletionOptions(false, 0.3, 1000);
        String system = "Ты — рекрутер. Имитируй собеседование на работу для указанной должности и желаемого уровня профессии, задавая вопросы, как будто ты потенциальный работодатель. " +
                "Твоя задача — определить технические и коммуникационные навыки кандидата. Сгенерируй 5 вопросов для интервью с потенциальным кандидатом. " +
                "Результат верни только в формате JSON-массива без каких-либо пояснений, такие параметры должны быть: [{\"question\": \"вопрос\"}]. " +
                "Отправь только JSON-массив без дополнительных предложений, начни ответ со скобок массива и закончи им.";
        ChatRequest chatRequest = ChatRequest.createWithUserAndSystemMessages("gpt://b1g1co7b2khdfuvhtsfc/yandexgpt", message, system, completionOptions);
        ChatResponse response = sendRequest(chatRequest);
        String result = ChatResponse.extractAssistantMessage(response);
        return questionService.parseQuestions(result);
    }

    @Transactional
    public AnswerResponse sendMessage(AnswerRequest answer, UserDetailsImpl user) throws IOException {
        UserEntity userEntity = userRepository.findById(user.getId()).get();

        if (questionRepository.existsByContentAndUser(answer.getQuestion(), userEntity)) {
            ChatRequest.CompletionOptions completionOptions = new ChatRequest.CompletionOptions(false, 0.3, 1000);
            String system = "Ты — рекрутер. Ты имитируешь собеседование на работу для указанной должности, задавая вопросы, как будто ты потенциальный работодатель. Твоя задача — определить технические навыки кандидата. " +
                    "Ты задал вопрос:" + answer.getQuestion() + " Получил следующий ответ от кандидата:\n" +
                    "\"" + answer.getAnswer() + "\"\n" +
                    "Проанализируй ответ кандидата, выдай следующие характеристики ответа: полнота ответа (число в процентах), удовлетворительность ответа (число в процентах), итоговая оценка (число в процентах), комментарии по улучшению ответа. Если число не целое, то пиши его через точку. Результат верни только в формате JSON без каких-либо пояснений, такие параметры должны быть: {\\\"completeness\\\": \\\"полнота ответа от 1 до 100\\\", \\\"satisfaction\\\": \\\"удовлетворительность ответа от 1 до 100\\\", \\\"score\\\": \\\"итоговая оценка ответа от 1 до 100\\\", \\\"improvement\\\": \\\"Подробные комментарии по улучшению ответа, рекомендации\\\"}. Отправь только JSON без дополнительных предложений";
            ChatRequest chatRequest = ChatRequest.createWithUserAndSystemMessages("gpt://b1g1co7b2khdfuvhtsfc/yandexgpt", answer.getProfession(), system, completionOptions);
            ChatResponse response = sendRequest(chatRequest);
            String result = ChatResponse.extractAssistantMessage(response);
            QuestionEntity questionCurrent = questionRepository.findByContentAndUser(answer.getQuestion(), userEntity);
            AnswerEntity answerEntity = questionService.parseAnswer(result);
            if (questionCurrent == null) {
                answerEntity.setQuestion(questionService.saveQuestion(answer.getQuestion(), userEntity, professionRepository.findByProfession(answer.getProfession())));
            } else {
                answerEntity.setQuestion(questionCurrent);
            }
            answerEntity.setContent(answer.getAnswer());
            answerEntity.setAnswerDate(LocalDateTime.now());
            answerRepository.save(answerEntity);
            return new AnswerResponse(answerEntity);
        } else {
            throw new MyEntityNotFoundException("question");
        }
    }

    public ChatResponse sendRequest(ChatRequest chatRequest) {
        String apiKey = "AQVN2qkiWIQ-vaszV_3jIOQ5E3XMoDBdOUXeqju0";
        return yandexGPTClient.generateText("Api-key " + apiKey, chatRequest);
    }
}

