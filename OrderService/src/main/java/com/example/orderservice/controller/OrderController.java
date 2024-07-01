package com.example.orderservice.controller;

import com.example.orderservice.config.OrderAppConfig;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducerOrder;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final OrderAppConfig orderAppConfig;
    private final KafkaProducerOrder kafkaProducerOrder;
    private  final OrderProducer orderProducer;

    @Autowired
    public OrderController(Environment env, OrderService orderService, OrderAppConfig orderAppConfig, KafkaProducerOrder kafkaProducerOrder, OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.orderAppConfig = orderAppConfig;
        this.kafkaProducerOrder = kafkaProducerOrder;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails) throws ExecutionException, InterruptedException {

        ModelMapper mapper = orderAppConfig.modelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        /* jpa */
//        OrderDto createdOrder = orderService.createOrder(orderDto);
//        ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        kafkaProducerOrder.send("example-product-topic",orderDto);
        orderProducer.send("orders", orderDto);
        log.info("send success");

        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(orderAppConfig.modelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("test")
    public String mainP(){ //test
        return String.format("order Controller Port %s", env.getProperty("local.server.port"));
    }
}
