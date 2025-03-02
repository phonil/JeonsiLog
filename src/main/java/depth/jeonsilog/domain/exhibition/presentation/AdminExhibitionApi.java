package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Admin Exhibition API", description = "Admin Exhibition 관련 API입니다.")
public interface AdminExhibitionApi {

    @Operation(summary = "전시회 상세 정보 및 전시 공간 정보 수정", description = "전시회 상세 정보 및 전시 공간 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping
    ResponseEntity<?> updateExhibitionDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UpdateExhibitionDetailReq와 UpdatePlaceWithExhibitionDetailReq를 참고해주세요", required = true) @RequestPart("updateExhibitionDetailReq") ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq,
            @Parameter(description = "등록할 전시회 포스터 이미지 파일을 입력해주세요.") @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException;

    @Operation(summary = "전시회 순서 변경", description = "관리자 페이지에서 전시회 순서를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/sequence")
    ResponseEntity<?> updateExhibitionSequence(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UpdateExhibitionSequenceList와 UpdateExhibitionSequence를 참고해주세요", required = true) @RequestBody ExhibitionRequestDto.UpdateExhibitionSequenceList requestDto
    );
}
