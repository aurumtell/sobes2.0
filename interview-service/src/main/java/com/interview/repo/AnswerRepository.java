package com.interview.repo;


import com.interview.model.entity.AnswerEntity;
import com.interview.model.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findAllByQuestionOrderByIdAsc(QuestionEntity question);
}

