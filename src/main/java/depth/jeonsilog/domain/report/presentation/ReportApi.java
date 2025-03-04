package depth.jeonsilog.domain.report.presentation;

import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Report API", description = "Report 관련 API입니다.")
public interface ReportApi {

    @Operation(summary = "감상평, 댓글 혹은 전시회 신고", description = "감상평, 댓글 혹은 포스터 등록 알림 신고입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신고 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "신고 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    ResponseEntity<Void> report(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 ReportReq를 참고해주세요", required = true) @RequestBody ReportRequestDto.ReportReq reportReq
    );
}
