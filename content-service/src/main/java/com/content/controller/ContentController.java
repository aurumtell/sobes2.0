package com.content.controller;


import com.content.model.request.AdviseRequest;
import com.content.model.request.ArticleRequest;
import com.content.model.response.AdviseResponse;
import com.content.model.response.ArticleResponse;
import com.content.service.ArticleAdviseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage articles",
        name = "Article Resource")
public class ContentController {

    @Autowired
    ArticleAdviseService articleAdviseService;

    @Operation(summary = "Get article",
            description = "Get article by Article Id")
    @GetMapping(value = "/user/article/{articleId}")
    @ResponseBody
    public ArticleResponse getArticleById(@PathVariable Long articleId){
        return articleAdviseService.getArticleById(articleId);
    }

    @Operation(summary = "Get article",
            description = "Get all articles")
    @GetMapping(value = "/user/article")
    @ResponseBody
    public List<ArticleResponse> getAllArticle(){
        return articleAdviseService.getAllArticle();
    }

    @Operation(summary = "Create article",
            description = "Create Article")
    @PostMapping(value = "/user/article")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ArticleResponse createArticle(@RequestBody ArticleRequest article){
        return articleAdviseService.createArticle(article);
    }

    @Operation(summary = "Create advise",
            description = "Create advise")
    @PostMapping(value = "/user/advise")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AdviseResponse createAdvise(@RequestBody AdviseRequest advise){
        return articleAdviseService.createAdvise(advise);
    }

    @Operation(summary = "Get advise",
            description = "Get all advises")
    @GetMapping(value = "/user/advise")
    @ResponseBody
    public List<AdviseResponse> getAllAdvise(){
        return articleAdviseService.getAllAdvise();
    }
}