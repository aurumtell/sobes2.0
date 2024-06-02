package com.content.service;


import com.content.exception.MyEntityNotFoundException;
import com.content.model.entity.AdviseEntity;
import com.content.model.request.AdviseRequest;
import com.content.model.request.ArticleRequest;
import com.content.model.response.AdviseResponse;
import com.content.repo.AdviseRepository;
import com.content.repo.ArticleRepository;
import com.content.model.entity.ArticleEntity;
import com.content.model.response.ArticleResponse;
import com.content.repo.CompanyRepository;
import com.content.repo.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleAdviseService {
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    CommentRepository commentRepository;
//
    @Autowired
    AdviseRepository adviseRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ProfessionRepository professionRepository;

    @Autowired
    CompanyRepository companyRepository;

//    public ArticleResponse createArticle(ArticleRequest articleRequest, UserDetailsImpl user) {
//        ArticleEntity articleEntity = new ArticleEntity();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        articleEntity.setCreatedBy(userEntity);
//        articleEntity.setCategory(categoryRepository.findById(articleRequest.getCategoryId()).get());
//        articleEntity.setTitle(articleRequest.getTitle());
//        articleEntity.setText(articleRequest.getText());
//        articleEntity.setDescription(articleRequest.getDescription());
//        articleEntity.setCreatedAt(LocalDateTime.now());
//        articleEntity.setDraft(articleRequest.getDraft());
//        articleEntity.setUsers(new HashSet<>());
//        return new ArticleResponse(articleRepository.save(articleEntity), userEntity);
//    }

    public ArticleResponse getArticleById(Long articleId) {

        ArticleEntity article = articleRepository.findById(articleId).orElseThrow(() -> new MyEntityNotFoundException("Article"));;
        return new ArticleResponse(article);
    }

    public List<ArticleResponse> getAllArticle() {
        ArticleResponse articleResponse = new ArticleResponse();
        return articleResponse.getListArticleResponces((articleRepository.findAll()));
    }

    public List<AdviseResponse> getAllAdvise() {
        AdviseResponse adviseResponse = new AdviseResponse();
        return adviseResponse.getListAdviseResponces((adviseRepository.findAll()));
    }

    public ArticleResponse createArticle(ArticleRequest article) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setAuthor(article.getAuthor());
        articleEntity.setContent(article.getContent());
        articleEntity.setLink(article.getLink());
        articleEntity.setTitle(article.getTitle());
        articleEntity.setProfession(professionRepository.findByProfession(article.getProfession()));
        return new ArticleResponse(articleRepository.save(articleEntity));
    }

    public AdviseResponse createAdvise(AdviseRequest advise) {
        AdviseEntity adviseEntity = new AdviseEntity();
        adviseEntity.setProfession(professionRepository.findByProfession(advise.getProfession()));
        adviseEntity.setText(advise.getText());
        adviseEntity.setName(advise.getName());
        adviseEntity.setLevel(advise.getLevel());
        adviseEntity.setCompany(companyRepository.findByCompany(advise.getCompany()));
        return new AdviseResponse(adviseRepository.save(adviseEntity));
    }

//    public List<ArticleResponse> getUserArticle(UserDetailsImpl user) {
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        ArticleResponse articleResponse = new ArticleResponse();
//        return articleResponse.getListArticleResponces(articleRepository.findAllByCreatedBy(userEntity), userEntity);
//    }
//
//    public List<ArticleResponse> getArticleByCategoryId(Integer categoryId, UserDetailsImpl user) {
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        ArticleResponse articleResponse = new ArticleResponse();
//        return articleResponse.getListArticleResponces((articleRepository.findAllByCategoryId(categoryId)), userEntity);
//    }
//
//    public List<ArticleResponse> getArticleByUserId(UserEntity user, UserEntity currentUser) {
//        ArticleResponse articleResponse = new ArticleResponse();
//        return articleResponse.getListArticleResponces((articleRepository.findAllByCreatedBy(user)), currentUser);
//    }
//

//
//    public List<CategoryEntity> getAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    public List<ArticleResponse> getUserArticleDrafts(UserDetailsImpl user) {
//        ArticleResponse articleResponse = new ArticleResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return articleResponse.getListArticleResponces((articleRepository.findAllByCreatedByAndDraft(userRepository.findById(user.getId()).get(), true)), userEntity);
//    }
//
//    public ArticleResponse createReaction(Long articleId, Boolean reaction, UserDetailsImpl user) {
//        ArticleEntity article = articleRepository.findById(articleId).get();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        if (article.getUsers().contains(userEntity)) {
//            if (reaction) {
//                // тут должна быть ошибка, что человек ставит реакцию, когда она уже есть
//                return null;
//            } else {
//                article.removeUser(userRepository.findById(user.getId()).get());
//            }
//        } else {
//            if (reaction) {
//                article.addUser(userRepository.findById(user.getId()).get());
//            } else {
//                // тут должна быть ошибка, что человек пытается убрать, когда ее нет
//                return null;
//            }
//        }
//        articleRepository.save(article);
//        return new ArticleResponse(article, userEntity);
//    }
//
////    public List<ArticleResponse> getSubscribtionArticles(UserDetailsImpl user) {
////        UserEntity userEntity = userRepository.findById(user.getId()).get();
////        return userEntity.getSubscriptions().stream().flatMap(s -> getArticleByUserId(s, userEntity).stream()).distinct().collect(Collectors.toList());
////    }
//
//    public CommentResponse createComment(Long articleId, UserDetailsImpl user) {
//        CommentEntity comment = new CommentEntity();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        comment.setArticle(articleRepository.findById(articleId).get());
//        comment.setUser(userEntity);
//        commentRepository.save(comment);
//        return new CommentResponse(comment, userEntity);
//    }
//
//    public List<CommentResponse> getAllComments(Long articleId, UserDetailsImpl user) {
//        CommentResponse commentResponse = new CommentResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return commentResponse.getListCommentResponces(commentRepository.findByArticle(articleRepository.findById(articleId).get()), userEntity);
//    }
//
//    public List<ArticleResponse> getSearchArticle(String line, UserDetailsImpl user) {
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        ArticleResponse articleResponse = new ArticleResponse();
//        return articleResponse.getListArticleResponces(articleRepository.findByTitleContainingIgnoreCase(line), userEntity);
//    }
}