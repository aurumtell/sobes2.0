package com.auth.repo;

import com.auth.model.entity.ProfessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, Integer> {
    Optional<ProfessionEntity> findByProfession(String profession);
}
