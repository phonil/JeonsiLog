package depth.jeonsilog.domain.stop.presentation;

import depth.jeonsilog.domain.stop.application.StopService;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stop")
public class StopController implements StopApi {

    private final StopService stopService;

    @GetMapping
    public ResponseEntity<?> findUserIsStop(
            @CurrentUser final UserPrincipal userPrincipal
    ) {
        return stopService.findUserIsStop(userPrincipal);
    }

    @PatchMapping
    public ResponseEntity<Void> updateIsFirstAccess(
            @CurrentUser final UserPrincipal userPrincipal
    ) {
        stopService.updateIsFirstAccess(userPrincipal);
        return ResponseEntity.noContent().build();
    }
}
