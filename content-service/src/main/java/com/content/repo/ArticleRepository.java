package com.content.repo;


import com.content.model.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findAllByProfessionId(Integer professionId);

//    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArticleEntity a JOIN a.users u WHERE u.email = :email")
//    boolean existsUserByEmail(String email);
//
//    List<ArticleEntity> findByTitleContainingIgnoreCase(String title);
}
