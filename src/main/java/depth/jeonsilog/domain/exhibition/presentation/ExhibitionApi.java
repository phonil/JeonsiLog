package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Exhibition API", description = "Exhibition 관련 API입니다.")
public interface ExhibitionApi {

    @Operation(summary = "전시회 목록 조회 - 요즘 뜨는 전시 목록 조회", description = "요즘 뜨는 전시 목록을 조회합니다. 관리자가 미리 설정해 둔 10개의 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/recently")
    ResponseEntity<?> findRecentlyExhibitionList(
            @Parameter(description = "전시회 목록을 조회합니다. 페이지 값 안주셔도 됩니다!! 조회되는 전시회 개수는 10개입니다.", required = false) @RequestParam(value = "page", defaultValue = "0") Integer page
    );

    @Operation(summary = "전시회 목록 조회 - 다채로운 예술의 향연 전시 목록 조회", description = "다채로운 예술의 향연 전시 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/colorful")
    ResponseEntity<?> findColorfulExhibitionList();

    @Operation(summary = "전시회 목록 조회 - 곧 종료되는 전시 목록 조회", description = "곧 종료되는 전시 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/endingSoon")
    ResponseEntity<?> findEndingSoonExhibitionList();

    @Operation(summary = "전시회 목록 조회 - 새로 시작한 전시 목록 조회", description = "새로 시작한 전시 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/new")
    ResponseEntity<?> findNewExhibitionList();


    @Operation(summary = "전시회 상세 정보 조회", description = "전시회 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{exhibitionId}")
    ResponseEntity<?> findExhibition(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    );

    @Operation(summary = "랜덤 전시회 2개 조회", description = "랜덤 전시회를 2개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExhibitionResponseDto.RandomExhibitionRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/random")
    ResponseEntity<?> randomTwoExhibitions();

    @Operation(summary = "검색어를 포함한 전시회 목록 조회", description = "검색어를 포함한 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/{searchWord}")
    ResponseEntity<?> searchExhibitions(
            @Parameter(description = "검색한 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @PathVariable(value = "searchWord") String searchWord
    );

    @Operation(summary = "전시회 포스터 조회", description = "전시회 포스터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.PosterRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/poster/{exhibitionId}")
    ResponseEntity<?> findPoster(
            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    );

    @Operation(summary = "전시회 이름으로 전시회 조회", description = "전시회 이름만으로 전시회를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.SearchExhibitionByNameResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/name/{searchWord}")
    ResponseEntity<?> searchExhibitionByName(
            @Parameter(description = "검색한 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "전시회 이름을 입력해주세요.", required = true) @PathVariable String searchWord
    );
}
