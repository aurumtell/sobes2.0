package com.interview.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "answers", schema = "public")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="questionid")
    private QuestionEntity question;

    @Column(name="improvement")
    private String improvement;

    @Column(name="content")
    private String content;

    @Column(name = "completeness")
    private Integer completeness;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    @Column(name = "score")
    private Double score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "answerdate")
    private LocalDateTime answerDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerEntity)) return false;
        return id != null && id.equals(((AnswerEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}