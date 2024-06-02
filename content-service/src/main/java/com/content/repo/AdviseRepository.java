package com.content.repo;


import com.content.model.entity.AdviseEntity;
import com.content.model.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviseRepository extends JpaRepository<AdviseEntity, Long> {

}
