package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController implements ExhibitionApi {

    private final ExhibitionService exhibitionService;

    @GetMapping("/recently")
    public ResponseEntity<?> findRecentlyExhibitionList(
             @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return exhibitionService.findRecentlyExhibitionList(page);
    }

    @GetMapping("/colorful")
    public ResponseEntity<?> findColorfulExhibitionList() {
        return exhibitionService.findColorfulExhibitionList();
    }

    @GetMapping("/endingSoon")
    public ResponseEntity<?> findEndingSoonExhibitionList() {
        return exhibitionService.findEndingSoonExhibitionList();
    }

    @GetMapping("/new")
    public ResponseEntity<?> findNewExhibitionList() {
        return exhibitionService.findNewExhibitionList();
    }

    @GetMapping("/{exhibitionId}")
    public ResponseEntity<?> findExhibition(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return exhibitionService.findExhibition(userPrincipal, exhibitionId);
    }

    @GetMapping("/random")
    public ResponseEntity<?> randomTwoExhibitions() {
        return exhibitionService.randomTwoExhibitions();
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchExhibitions(
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "searchWord") String searchWord
    ) {
        return exhibitionService.searchExhibitions(page, searchWord);
    }

    @PatchMapping
    public ResponseEntity<?> updateExhibitionDetail(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart("updateExhibitionDetailReq") ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq,
             @RequestPart(value = "img", required = false) MultipartFile img
            ) throws IOException {
        return exhibitionService.updateExhibitionDetail(userPrincipal, updateExhibitionDetailReq, img);
    }

    @GetMapping("/poster/{exhibitionId}")
    public ResponseEntity<?> findPoster(
            @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return exhibitionService.findPoster(exhibitionId);
    }

    @GetMapping("/search/name/{searchWord}")
    public ResponseEntity<?> searchExhibitionByName(
            @RequestParam(value = "page") Integer page,
            @PathVariable String searchWord
    ) {
        return exhibitionService.searchExhibitionsByName(page, searchWord);
    }

    @PatchMapping("/sequence")
    public ResponseEntity<?> updateExhibitionSequence(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ExhibitionRequestDto.UpdateExhibitionSequenceList requestDto
    ) {
        return exhibitionService.updateExhibitionSequence(userPrincipal, requestDto);
    }
}
