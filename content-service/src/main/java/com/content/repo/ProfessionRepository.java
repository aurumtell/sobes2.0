package com.content.repo;

import com.content.model.entity.ProfessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, Long> {

    ProfessionEntity findByProfession(String profession);
}
