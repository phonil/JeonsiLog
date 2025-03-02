package depth.jeonsilog.domain.review.presentation;

import depth.jeonsilog.domain.review.application.ReviewService;
import depth.jeonsilog.domain.review.dto.ReviewRequestDto;
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
@RequestMapping("/api/reviews")
public class ReviewController implements ReviewApi {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> writeReview(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody ReviewRequestDto.WriteReviewReq writeReviewReq
            ) throws IOException {
        reviewService.writeReview(userPrincipal, writeReviewReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(userPrincipal, reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exhibition/{exhibitionId}")
    public ResponseEntity<?> getReviewList(
            @RequestParam(value = "page") Integer page,
            @PathVariable Long exhibitionId
    ) {
        return reviewService.getReviewList(page, exhibitionId);
    }

    @GetMapping
    public ResponseEntity<?> getMyReviewList(
            @RequestParam(value = "page") Integer page,
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return reviewService.getMyReviewList(page, userPrincipal);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserReviewList(
            @RequestParam(value = "page") Integer page,
            @PathVariable Long userId
    ) {
        return reviewService.getUserReviewList(page, userId);
    }

    @GetMapping("/check/{exhibitionId}")
    public ResponseEntity<?> getUserReviewList(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return reviewService.checkIsWrite(userPrincipal, exhibitionId);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<?> getReview(
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        return reviewService.getReview(reviewId);
    }

    @PatchMapping
    public ResponseEntity<Void> updateReview(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody ReviewRequestDto.UpdateReviewReq updateReviewReq
    ) {
        reviewService.updateReview(userPrincipal, updateReviewReq);
        return ResponseEntity.noContent().build();
    }
}
