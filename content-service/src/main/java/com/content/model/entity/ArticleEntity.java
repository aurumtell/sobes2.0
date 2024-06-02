package com.content.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "articles", schema = "public")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="professionid")
    private ProfessionEntity profession;

    @Column(name = "title")
    private String title;


    @Column(name = "author")
    private String author;

    @Column(name = "content")
    private String content;

    @Column(name = "link")
    private String link;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleEntity)) return false;
        return id != null && id.equals(((ArticleEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}