package org.wingstudio.blog.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/t1")
    public String get(){
        return "get";
    }
    @PostMapping("/t1")
    public String post(){
        return "post";
    }
//    @GetMapping("/{id}")
//    public String get1(@PathVariable("id")String id){
//        return id+"get";
//    }

}
