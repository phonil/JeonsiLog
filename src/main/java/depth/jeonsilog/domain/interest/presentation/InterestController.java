package depth.jeonsilog.domain.interest.presentation;

import depth.jeonsilog.domain.interest.application.InterestService;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interest")
public class InterestController implements InterestApi {

    private final InterestService interestService;

    @PostMapping("/{exhibitionId}")
    public ResponseEntity<?> registerInterest(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long exhibitionId
    ) {
        return interestService.registerInterest(userPrincipal, exhibitionId);
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<Void> deleteInterest(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long exhibitionId
    ) {
        interestService.deleteInterest(userPrincipal, exhibitionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> deleteInterest(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return interestService.getInterestList(page, userPrincipal);
    }
}
