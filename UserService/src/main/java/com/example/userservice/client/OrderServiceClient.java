package com.example.userservice.client;

import com.example.userservice.member.dto.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="order")
public interface OrderServiceClient {


    @GetMapping("order/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable Long userId);
}
