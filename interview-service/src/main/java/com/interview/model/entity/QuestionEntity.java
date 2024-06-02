package com.interview.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "questions", schema = "public")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="userid")
    private UserEntity user;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="professionid")
    private ProfessionEntity profession;


    @OneToMany(mappedBy="question", fetch= FetchType.EAGER)
    private Set<AnswerEntity> answers;

    @Column(name = "content")
    private String content;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionEntity)) return false;
        return id != null && id.equals(((QuestionEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}