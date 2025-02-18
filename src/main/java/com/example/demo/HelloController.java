package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Marks this as a REST controller
@RequestMapping("/api")  // Base URL path for this controller
public class HelloController {

    @GetMapping("/hello")  // Maps GET requests to /api/hello
    public String sayHello() {
        return "Hello, World!";
    }
}
