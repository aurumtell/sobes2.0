package com.auth.repo;


import com.auth.model.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
    Optional<LevelEntity> findByExperience(String experince);
}
