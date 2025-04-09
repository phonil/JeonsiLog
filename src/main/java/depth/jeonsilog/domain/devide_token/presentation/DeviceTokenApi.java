package depth.jeonsilog.domain.devide_token.presentation;

import depth.jeonsilog.domain.devide_token.dto.DeviceTokenRequestDto;
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
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Device Token API", description = "Device Token 관련 API입니다.")
public interface DeviceTokenApi {

    @Operation(summary = "기기 토큰 등록", description = "기기 토큰을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "기기 토큰 등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "기기 토큰 등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    ResponseEntity<Void> registerToken(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 DeviceTokenReq를 확인해주세요.", required = true) @RequestBody DeviceTokenRequestDto.DeviceTokenReq deviceTokenReq
    );
}
