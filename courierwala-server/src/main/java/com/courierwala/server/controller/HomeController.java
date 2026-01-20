package com.courierwala.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HomeController {

    @GetMapping("/a")
    public String hello() {
        System.out.println("tip tip");
        String s = "fkjewnfkj";
        return s;
    }
}
