package com.example.productservice.article.controller;

import com.example.productservice.article.dto.ArticleForm;
import com.example.productservice.article.entity.Article;
import com.example.productservice.article.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/all") //물품 모두 조회
    public List<Article> index() {
        return articleService.index();
    }

    @GetMapping("{article_id}") //물품 상세 조회
    public Article show(@PathVariable Long article_id){
        return articleService.show(article_id);
    }

    @PostMapping("/create")
    public ResponseEntity<Article> create(@RequestBody ArticleForm ArticleDto) {
        Article cratedArticle = articleService.createArticle(ArticleDto);
        return (cratedArticle != null) ?
                ResponseEntity.status(HttpStatus.OK).body(cratedArticle) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
