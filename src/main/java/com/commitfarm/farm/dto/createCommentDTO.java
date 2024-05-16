package com.commitfarm.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;


@Data
@Schema(description = "Post: 댓글 생성 DTO")
@Getter
public class createCommentDTO {

    @Schema(description = "댓글 내용", example = "API 에러 수정 요청")
    private String comment;
}
