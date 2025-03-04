package depth.jeonsilog.domain.auth.presentation;

import depth.jeonsilog.domain.auth.dto.AuthRequestDto;
import depth.jeonsilog.domain.auth.dto.AuthResponseDto;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Authorization 관련 API입니다.")
public interface AuthApi {

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/sign-up")
    ResponseEntity<Void> signup(
            @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody AuthRequestDto.SignUpReq signUpRequest
    );

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.AuthRes.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/sign-in")
    ResponseEntity<?> signin(
            @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @Valid @RequestBody AuthRequestDto.SignInReq signInRequest
    );

    @Operation(summary = "토큰 갱신", description = "신규 토큰 갱신을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.AuthRes.class))}),
            @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/refresh")
    ResponseEntity<?> refresh(
            @Parameter(description = "Schemas의 RefreshTokenRequest를 참고해주세요.", required = true) @Valid @RequestBody AuthRequestDto.RefreshTokenReq tokenRefreshRequest
    );


    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping(value = "/sign-out")
    ResponseEntity<Void> signOut(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    );

    @Operation(summary = "닉네임 중복 체크", description = "닉네임이 중복인지 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 체크 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.NicknameCheckRes.class))}),
            @ApiResponse(responseCode = "400", description = "중복 체크 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/nickname/{nickname}")
    ResponseEntity<?> checkNickname(
            @Parameter(description = "검사할 닉네임을 입력해주세요.", required = true) @PathVariable(value = "nickname") String nickname
    );

}
