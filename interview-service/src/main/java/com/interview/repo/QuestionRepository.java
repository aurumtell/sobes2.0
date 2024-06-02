package com.interview.repo;


import com.interview.model.entity.QuestionEntity;
import com.interview.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {


    List<QuestionEntity> findAllByProfessionIdAndUserId(Long professionId, Long userId);

    QuestionEntity findByContentAndUser(String content, UserEntity user);
    List<QuestionEntity> findAllByContentAndUser(String content, UserEntity user);

    boolean existsByContentAndUser(String content, UserEntity user);
}

