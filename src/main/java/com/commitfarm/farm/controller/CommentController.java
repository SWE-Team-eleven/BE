package com.commitfarm.farm.controller;


import com.commitfarm.farm.dto.createCommentDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

//Swagger read annotation @Schema @Parameter
@Tag(name = "댓글 API", description = "댓글 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {


    @PostMapping("/create/{usersId}/{ticketId}")
public Result<?> createComment(@PathVariable Long usersId, @PathVariable Long ticketId , @RequestBody @RequestParam createCommentDTO dto){ //
        return new Result<>("createCommentdto");
    }





    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
