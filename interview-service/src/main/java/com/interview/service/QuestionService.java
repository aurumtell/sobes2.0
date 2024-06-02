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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    public List<QuestionResponse> getQuestionsByProfession(String profession, UserDetailsImpl user) {
        QuestionResponse questionResponse = new QuestionResponse();
        return questionResponse.getListQuestionResponses(questionRepository.findAllByProfessionIdAndUserId(professionRepository.findByProfession(profession).getId(), user.getId()));
    }

    public AnswerEntity parseAnswer(String json) throws IOException {
        return mapper.readValue(json, AnswerEntity.class);
    }

//    public List<GenerateResponse> parseQuestions(String json) throws JsonProcessingException {
//        return Arrays.asList(mapper.readValue(json, GenerateResponse[].class));
//    }

    public List<GenerateResponse> parseQuestions(String json) throws JsonProcessingException {
        // Регулярное выражение для поиска JSON-массива
        Pattern pattern = Pattern.compile("\\[.*\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String jsonArray = matcher.group();
            return Arrays.asList(mapper.readValue(jsonArray, GenerateResponse[].class));
        } else {
            throw new JsonProcessingException("JSON array not found") {};
        }
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
