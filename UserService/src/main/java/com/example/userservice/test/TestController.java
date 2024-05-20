package com.example.userservice.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/user")
@Slf4j
public class TestController {
    @GetMapping
    public String mainP(){
        return "user Controller";
    }

    @GetMapping("/test")
    public String message(@RequestHeader("user-request") String header){
        log.info(header);
        return "user service";
    }
}
