package com.commitfarm.farm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api/users")
public class UsersController {

    @PostMapping("/signup")
    Result<?> signup(){
        return new Result<>("signup");
    }


    @PostMapping("/login")
    public Result<?> login(){
        return new Result<>("login");
    }
    @GetMapping("/logout")
    public Result<?> logout(){
        return new Result<>("logout");
    }










    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


}
