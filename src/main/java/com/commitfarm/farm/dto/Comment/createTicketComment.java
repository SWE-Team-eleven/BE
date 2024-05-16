package com.commitfarm.farm.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class createTicketComment {
    @Schema(description = "댓글 내용", example = "API 에러 수정 요청")
    private String comment;

}
