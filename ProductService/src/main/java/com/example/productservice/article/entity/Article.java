package com.example.productservice.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String status;
    @Column
    private Integer price;
    @Column
    private Integer quantity;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Article(String name, String status, int price, int quantity) {
        this.name = name;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
    }

    @PrePersist //영구 저장소에 저장되기 전에 실행되는 메서드
    protected void onCreate(){
        createdAt = new Date();
    }

    @PreUpdate //업데이트되기 전에 실행되는 메서드
    protected void onUpdate(){
        updatedAt = new Date();
    }


}
