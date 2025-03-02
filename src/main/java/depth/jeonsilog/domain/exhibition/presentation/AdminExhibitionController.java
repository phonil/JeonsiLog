package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.application.AdminExhibitionService;
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
@RequestMapping("/api/admin/exhibitions")
public class AdminExhibitionController implements AdminExhibitionApi {

    private final AdminExhibitionService adminExhibitionService;

    @PatchMapping
    public ResponseEntity<Void> updateExhibitionDetail(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart("updateExhibitionDetailReq") ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        adminExhibitionService.updateExhibitionDetail(userPrincipal, updateExhibitionDetailReq, img);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/sequence")
    public ResponseEntity<Void> updateExhibitionSequence(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ExhibitionRequestDto.UpdateExhibitionSequenceList requestDto
    ) {
        adminExhibitionService.updateExhibitionSequence(userPrincipal, requestDto);
        return ResponseEntity.noContent().build();
    }
}
