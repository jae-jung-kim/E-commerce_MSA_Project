package com.example.productservice.article.repository;

import com.example.productservice.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Article findByProductId(String productId);
}
