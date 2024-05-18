package com.example.productservice.article.service;

import com.example.productservice.article.dto.ArticleForm;
import com.example.productservice.article.entity.Article;
import com.example.productservice.article.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    //물품 전체 조회
    public List<Article> index() {
        return articleRepository.findAll();
    }

    //물품 상세 조회
    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article createArticle(ArticleForm dto) {
        Article article = dto.toEntity();
        if(article.getId() != null){
            return null;
        }
        return articleRepository.save(article);
    }
}
