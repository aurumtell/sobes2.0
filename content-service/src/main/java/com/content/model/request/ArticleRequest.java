package com.content.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    private String profession;
    private String content;
    private String author;
    private String link;
}
