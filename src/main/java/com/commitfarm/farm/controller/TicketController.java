package com.commitfarm.farm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@Tag(name = "티켓 API", description = "티켓 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api/ticket")
public class TicketController {


// ticket crud

    @PostMapping("/create")
    public Result<?> createTicket(){
        return new Result<>("createTicket");
    }
    @GetMapping("/read")
    public Result<?> readTicket(){
        return new Result<>("readTicket");
    }

    @PutMapping("/update") // may de not use
    public Result<?> updateTicket(){
        return new Result<>("updateTicket");
    }

    @DeleteMapping("/delete")//
    public Result<?> deleteTicket(){
        return new Result<>("deleteTicket");
    }







    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}
