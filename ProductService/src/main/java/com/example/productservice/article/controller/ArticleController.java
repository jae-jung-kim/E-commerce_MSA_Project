package com.example.productservice.article.controller;

import com.example.productservice.article.dto.ArticleDto;
import com.example.productservice.article.dto.ResponseArticle;
import com.example.productservice.article.entity.Article;
import com.example.productservice.article.service.ArticleService;
import com.example.productservice.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final Environment env;
    private final AppConfig appConfig;

    public ArticleController(ArticleService articleService, Environment env, AppConfig appConfig) {
        this.articleService = articleService;
        this.env = env;
        this.appConfig = appConfig;
    }

    @GetMapping("/all") //물품 모두 조회
    public ResponseEntity<List<ResponseArticle>> getAllProducts() {
        Iterable<Article> articleList = articleService.getAllProducts();

        List<ResponseArticle> result = new ArrayList<>();
        articleList.forEach(v -> {
            result.add(appConfig.modelMapper().map(v, ResponseArticle.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("{article_id}") //물품 상세 조회
    public Article show(@PathVariable Long article_id){
        return articleService.show(article_id);
    }

    @PostMapping("/create")
    public ResponseEntity<Article> create(@RequestBody ArticleDto ArticleDto) {
        Article cratedArticle = articleService.createArticle(ArticleDto);
        return (cratedArticle != null) ?
                ResponseEntity.status(HttpStatus.OK).body(cratedArticle) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("test")
    public String mainP(){ //test
        return String.format("article Controller Port %s", env.getProperty("local.server.port"));
    }
}
