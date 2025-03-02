package depth.jeonsilog.domain.follow.presentation;

import depth.jeonsilog.domain.follow.application.FollowService;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follows")
public class FollowController implements FollowApi {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> follow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) throws IOException {
        followService.follow(userPrincipal, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteFollow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) {
        followService.deleteFollowing(userPrincipal, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/follower/{userId}")
    public ResponseEntity<Void> deleteFollower(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) {
        followService.deleteFollower(userPrincipal, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/following")
    public ResponseEntity<?> findMyFollowingList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return followService.findMyFollowingList(page, userPrincipal);
    }

    @GetMapping("/follower")
    public ResponseEntity<?> findMyFollowerList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return followService.findMyFollowerList(page, userPrincipal);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> findUserFollowingList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "userId") Long userId
    ) {
        return followService.findUserFollowingList(page, userPrincipal, userId);
    }

    @GetMapping("/follower/{userId}")
    public ResponseEntity<?> findUserFollowerList(
            @CurrentUser UserPrincipal userPrincipal,
            Integer page,
            @PathVariable(value = "userId") Long userId
    ) {
        return followService.findUserFollowerList(page, userPrincipal, userId);
    }
}
