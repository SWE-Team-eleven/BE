package com.commitfarm.farm.dto.ticket.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosedTicketListRes {

    @Schema(description = "티켓 이름")
    private String title;

    @Schema(description = "티켓 상태")
    private String status;

    @Schema(description = "티켓 생성 시간")
    private String createdTime;
}
