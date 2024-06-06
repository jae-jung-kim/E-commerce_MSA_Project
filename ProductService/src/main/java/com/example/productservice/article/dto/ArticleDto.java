package com.example.productservice.article.dto;

import com.example.productservice.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor // 클래스 안쪽의 모든 필드를 매개변수로 하는 생성자 자동생성.
@NoArgsConstructor
@Setter
@ToString
public class ArticleDto {


    private String name;
    private String status;
    private int price;
    private int quantity;

    public Article toEntity() {
        return new Article(name, status, price, quantity);
    }
}
