package depth.jeonsilog.domain.rating.presentation;

import depth.jeonsilog.domain.rating.application.RatingService;
import depth.jeonsilog.domain.rating.dto.RatingRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController implements RatingApi {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<?> registerInterest(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
            ) throws IOException {
        return ratingService.registerRating(userPrincipal, ratingReq);
    }

    @PatchMapping
    public ResponseEntity<?> updateRating(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
    ) {
        return ratingService.updateRating(userPrincipal, ratingReq);
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<?> deleteRating(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long exhibitionId
    ) {
        return ratingService.deleteRating(userPrincipal, exhibitionId);
    }

    @GetMapping
    public ResponseEntity<?> getMyRatingList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return ratingService.getMyRatingList(page, userPrincipal);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserRatingList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page,
            @PathVariable Long userId
    ) {
        return ratingService.getUserRatingList(page, userPrincipal, userId);
    }
}
