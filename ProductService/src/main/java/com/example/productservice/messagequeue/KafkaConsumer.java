package com.example.productservice.messagequeue;

import com.example.productservice.article.entity.Article;
import com.example.productservice.article.repository.ArticleRepository;
import com.example.productservice.config.ProductAppConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    ArticleRepository articleRepository;
    ProductAppConfig productAppCOnfig;
    @Autowired
    public KafkaConsumer(ArticleRepository articleRepository, ProductAppConfig productAppCOnfig) {
        this.articleRepository = articleRepository;
        this.productAppCOnfig = productAppCOnfig;
    }

    //토픽에 데이터가 업데이트되면 수량 데이터가 같이 업데이트 됨.
    @KafkaListener(topics = "example-product-topic")
    public void updateQty(String kafkaMessage){
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        try{
            map = productAppCOnfig.objectMapper().readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch(JsonProcessingException ex){
            ex.printStackTrace();
        }

        Article article = articleRepository.findByProductId((String)map.get("productId")); //catalogs Microservice 수정 강의랑 조금 달라서 바꿔야할수도있음
        if(article != null){
            article.setQuantity(article.getQuantity() - (Integer) map.get("qty"));
            articleRepository.save(article);
        }
    }
}
