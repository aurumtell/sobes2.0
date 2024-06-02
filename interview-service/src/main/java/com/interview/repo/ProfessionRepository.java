package com.interview.repo;


import com.interview.model.entity.ProfessionEntity;
import com.interview.model.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, Long> {

    ProfessionEntity findByProfession(String message);
}

