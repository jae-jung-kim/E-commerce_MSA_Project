package com.example.productservice.article.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseArticle {
    private Long id;
    private String name;
    private String status;
    private Integer price;
    private Integer quantity;
    private Date createdAt;
}
