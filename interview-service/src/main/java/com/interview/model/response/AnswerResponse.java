package com.interview.model.response;


import com.interview.model.entity.AnswerEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerResponse {

    private Long id;
    private String improvement;
    private Integer completeness;
    private Integer satisfaction;
    private Double score;
    private String answer;

    public AnswerResponse(AnswerEntity answerEntity) {
        this.id = answerEntity.getId();
        this.improvement = answerEntity.getImprovement();
        this.completeness = answerEntity.getCompleteness();
        this.satisfaction = answerEntity.getSatisfaction();
        this.score = answerEntity.getScore();
        this.answer = answerEntity.getContent();
    }

    public List<AnswerResponse> getListAnswerResponses(List<AnswerEntity> answerEntities) {
        List<AnswerResponse> answerResponses = new ArrayList<>();
        for (AnswerEntity answerEntity: answerEntities) {
            answerResponses.add(new AnswerResponse(answerEntity));
        }
        return answerResponses;
    }
}