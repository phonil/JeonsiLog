package depth.jeonsilog.domain.follow.presentation;

import depth.jeonsilog.domain.follow.application.FollowService;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follows")
public class FollowController implements FollowApi {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> follow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) throws IOException {
        return followService.follow(userPrincipal, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteFollow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) {
        return followService.deleteFollowing(userPrincipal, userId);
    }

    @DeleteMapping("/follower/{userId}")
    public ResponseEntity<?> deleteFollower(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) {
        return followService.deleteFollower(userPrincipal, userId);
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
