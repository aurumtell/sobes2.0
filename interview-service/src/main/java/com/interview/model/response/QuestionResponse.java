package com.interview.model.response;


import com.interview.model.entity.ProfessionEntity;
import com.interview.model.entity.QuestionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponse {

    private Long id;
    private String content;
    private ProfessionEntity profession;

    public QuestionResponse(QuestionEntity question) {
        this.id = question.getId();
        this.profession = question.getProfession();
        this.content = question.getContent();
    }

    public List<QuestionResponse> getListQuestionResponses(List<QuestionEntity> questionEntities) {
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (QuestionEntity questionEntity: questionEntities) {
            questionResponses.add(new QuestionResponse(questionEntity));
        }
        return questionResponses;
    }
}