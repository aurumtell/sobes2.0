package com.interview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.exception.MyEntityNotFoundException;
import com.interview.model.entity.*;
import com.interview.model.response.AnswerResponse;
import com.interview.model.response.GenerateResponse;
import com.interview.model.response.QuestionResponse;
import com.interview.repo.AnswerRepository;
import com.interview.repo.ProfessionRepository;
import com.interview.repo.QuestionRepository;
import com.interview.repo.UserRepository;
import com.interview.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ProfessionRepository professionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public QuestionEntity saveQuestion(String question, UserEntity user, ProfessionEntity profession) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setProfession(profession);
        questionEntity.setContent(question);
        questionEntity.setUser(user);
        return questionRepository.save(questionEntity);
    }

    public static String parseJsonToQuestion(String jsonString) {
        String question = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Предполагаем, что jsonString представляет собой объект, содержащий ключ "question".
            // Если jsonString представляет собой массив, вам потребуется изменить эту часть.
            if (jsonNode.has("question")) {
                question = jsonNode.get("question").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return question;
    }

    public static Set<String> parseJsonToSet(String jsonString) {
        Set<String> questions = new HashSet<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            for (JsonNode node : jsonNode) {
                String question = node.get("question").asText();
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<QuestionResponse> getQuestionsByProfession(String profession, UserDetailsImpl user) {
        QuestionResponse questionResponse = new QuestionResponse();
        return questionResponse.getListQuestionResponses(questionRepository.findAllByProfessionIdAndUserId(professionRepository.findByProfession(profession).getId(), user.getId()));
    }

    public AnswerEntity parseAnswer(String json) throws IOException {
        return mapper.readValue(json, AnswerEntity.class);
    }

    public List<GenerateResponse> parseQuestions(String json) throws JsonProcessingException {
        return Arrays.asList(mapper.readValue(json, GenerateResponse[].class));
    }
    public List<AnswerResponse> getDialogByQuestion(String question, UserDetailsImpl user) {
        QuestionEntity questionEntity = questionRepository.findByContentAndUser(question, userRepository.findById(user.getId())
                .orElseThrow(() -> new MyEntityNotFoundException("user")));
        if (questionEntity == null) {
            throw new MyEntityNotFoundException("question");
        }
        else {
            return new AnswerResponse().getListAnswerResponses(answerRepository.findAllByQuestionOrderByIdAsc(questionEntity));
        }
    }
}
