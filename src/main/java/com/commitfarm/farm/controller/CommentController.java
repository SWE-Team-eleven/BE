package com.commitfarm.farm.controller;


import com.commitfarm.farm.dto.createCommentDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

// Swagger read annotation @Schema @Parameter
@Tag(name = "댓글 API", description = "댓글 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {



    @PostMapping("/create/milestone/{usersId}/{ticketId}")
public Result<?> createMilestoneComment(@PathVariable Long usersId, @PathVariable Long ticketId , @RequestBody @RequestParam createCommentDTO dto){ //
        return new Result<>("createCommentdto");
    }

    @PostMapping("/create/ticket/{usersId}/{ticketId}")
    public Result<?> createTicketComment(@PathVariable Long usersId, @PathVariable Long ticketId , @RequestBody @RequestParam createCommentDTO dto){ //
        return new Result<>("createCommentdto");
    }

    @PostMapping("/create/project/{usersId}/{ticketId}")
    public Result<?> createProjectComment(@PathVariable Long usersId, @PathVariable Long ticketId , @RequestBody @RequestParam createCommentDTO dto){ //
        return new Result<>("createCommentdto");
    }


    @GetMapping
    public Result<?> readCommentList(){
        return new Result<>("getCommentList");
    }
    @PutMapping("/update")
    public Result<?> updateCommentResult(@PathVariable Long commentId, @RequestBody @RequestParam createCommentDTO dto){
        return new Result<>("updateCommentResult");
    }

    @DeleteMapping("/delete")
    public Result<?> deleteCommentResult(@PathVariable Long commentId){
        return new Result<>("deleteCommentResult");
    }






    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

}
