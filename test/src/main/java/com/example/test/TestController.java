package com.example.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class TestController {
    @GetMapping("/test")
    public String mainP(){
        return "test Controller";
    }
}
