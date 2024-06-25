package com.example.productservice.messagequeue;

import com.example.productservice.article.entity.Article;
import com.example.productservice.article.repository.ArticleRepository;
import com.example.productservice.config.AppConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.lang.runtime.ObjectMethods;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class KafkaConsumer {
    ArticleRepository articleRepository;
    AppConfig appCOnfig;
    @Autowired
    public KafkaConsumer(ArticleRepository articleRepository, AppConfig appCOnfig) {
        this.articleRepository = articleRepository;
        this.appCOnfig = appCOnfig;
    }

    //토픽에 데이터가 업데이트되면 수량 데이터가 같이 업데이트 됨.
    @KafkaListener(topics = "example-product-topic")
    public void updateQty(String kafkaMessage){
        log.info("Kafka Message: ->" + kafkaMessage);
        ObjectMapper mapper = new ObjectMapper();

        Map<Object, Object> map = new HashMap<>();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
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
