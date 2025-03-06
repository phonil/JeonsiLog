package depth.jeonsilog.domain.stop.presentation;

import depth.jeonsilog.domain.stop.dto.StopRequestDto;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Admin Stop API", description = "Admin Stop 관련 API입니다.")
public interface AdminStopApi {

    @Operation(summary = "유저 정지", description = "유저를 정지합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정지 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "정지 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    ResponseEntity<Void> stopUser(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser final UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 StopUserReq를 참고해주세요.", required = true) @Valid @RequestBody final StopRequestDto.StopUserReq dto
    );
}
