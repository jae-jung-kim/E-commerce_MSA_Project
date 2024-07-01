package com.example.orderservice.messagequeue;

import com.example.orderservice.config.OrderAppConfig;
import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.TimestampedException;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaProducerOrder {
    private final KafkaTemplate<String,  String>  kafkaTemplate;
    private final OrderAppConfig orderAppConfig;

    @Autowired
    public KafkaProducerOrder(KafkaTemplate<String, String> kafkaTemplate, OrderAppConfig orderAppConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderAppConfig = orderAppConfig;
    }

    public OrderDto send(String topic, OrderDto orderDto){
        String jsonInString = "";
        try{
            jsonInString = orderAppConfig.objectMapper().writeValueAsString(orderDto);
        }catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }
        log.info("say hello");
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the Order microService: " + orderDto);

        return  orderDto;
    }
}
