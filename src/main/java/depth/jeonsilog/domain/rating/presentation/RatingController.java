package depth.jeonsilog.domain.rating.presentation;

import depth.jeonsilog.domain.rating.application.RatingService;
import depth.jeonsilog.domain.rating.dto.RatingRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController implements RatingApi {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> registerInterest(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
            ) throws IOException {
        ratingService.registerRating(userPrincipal, ratingReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateRating(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
    ) {
        ratingService.updateRating(userPrincipal, ratingReq);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<Void> deleteRating(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long exhibitionId
    ) {
        ratingService.deleteRating(userPrincipal, exhibitionId);
        return ResponseEntity.noContent().build();
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
