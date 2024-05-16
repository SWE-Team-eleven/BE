package com.commitfarm.farm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;


@Tag(name = "프로젝트 API", description = "프로젝트 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

    @PostMapping("/create")
    Result<?> createProject(){
        return new Result<>("createProject");
    }

    @PutMapping("/update")
    public Result<?> updateProject(){
        return new Result<>("updateProject");
    }

    @GetMapping("/read")
    public Result<?> readProject(){
        return new Result<>("readProject");
    }

    @DeleteMapping("/delete")
    public Result<?> deleteProject(){
        return new Result<>("deleteProject");
    }





    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
