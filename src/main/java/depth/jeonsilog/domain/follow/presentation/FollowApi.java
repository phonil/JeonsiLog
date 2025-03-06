package depth.jeonsilog.domain.follow.presentation;

import depth.jeonsilog.domain.follow.dto.FollowResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Follow API", description = "Follow 관련 API입니다.")
public interface FollowApi {

    @Operation(summary = "팔로우", description = "User ID를 이용하여 특정 유저를 팔로우합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팔로우 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "팔로우 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{userId}")
    ResponseEntity<Void> follow(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) throws IOException;

    @Operation(summary = "팔로우 취소", description = "User ID를 이용하여 특정 유저를 팔로우 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팔로우 취소 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "팔로우 취소 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteFollow(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우 취소할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    );

    @Operation(summary = "팔로워 삭제", description = "User ID를 이용하여 팔로워를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팔로워 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "팔로워 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/follower/{userId}")
    ResponseEntity<Void> deleteFollower(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우 취소할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    );

    @Operation(summary = "나의 팔로잉 목록 조회", description = "나의 팔로잉 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.MyFollowingListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/following")
    ResponseEntity<?> findMyFollowingList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "나의 팔로잉 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page
    );

    @Operation(summary = "나의 팔로워 목록 조회", description = "나의 팔로워 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.MyFollowerListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/follower")
    ResponseEntity<?> findMyFollowerList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "나의 팔로워 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page
    );

    @Operation(summary = "타 유저 팔로잉 목록 조회", description = "타 유저의 팔로잉 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.UserFollowingListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/following/{userId}")
    ResponseEntity<?> findUserFollowingList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "타 유저의 팔로잉 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "조회할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    );

    @Operation(summary = "타 유저 팔로워 목록 조회", description = "타 유저의 팔로워 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.UserFollowerListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/follower/{userId}")
    ResponseEntity<?> findUserFollowerList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "타 유저의 팔로워 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "조회할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    );

}
