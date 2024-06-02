package com.content.model.response;


import com.content.model.entity.ArticleEntity;
import com.content.model.entity.ProfessionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponse {

    private Long id;
    private String profession;
    private String title;
    private String author;
    private String content;
    private String link;


    public ArticleResponse(ArticleEntity article) {
        this.id = article.getId();
        this.profession = article.getProfession().getProfession();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.link = article.getLink();

    }

    public List<ArticleResponse> getListArticleResponces(List<ArticleEntity> articleEntities) {
        List<ArticleResponse> articleResponses = new ArrayList<>();
        for (ArticleEntity articleEntity: articleEntities) {
            articleResponses.add(new ArticleResponse(articleEntity));
        }
        return articleResponses;
    }
}