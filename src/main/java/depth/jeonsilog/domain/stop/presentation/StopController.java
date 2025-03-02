package depth.jeonsilog.domain.stop.presentation;

import depth.jeonsilog.domain.stop.application.StopService;
import depth.jeonsilog.domain.stop.dto.StopRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stop")
public class StopController implements StopApi {

    private final StopService stopService;

    @PostMapping
    public ResponseEntity<?> stopUser(
            @CurrentUser final UserPrincipal userPrincipal,
            @Valid @RequestBody final StopRequestDto.StopUserReq dto
    ) {
        return stopService.stopUser(userPrincipal, dto);
    }

    @GetMapping
    public ResponseEntity<?> findUserIsStop(
            @CurrentUser final UserPrincipal userPrincipal
    ) {
        return stopService.findUserIsStop(userPrincipal);
    }

    @PatchMapping
    public ResponseEntity<?> updateIsFirstAccess(
            @CurrentUser final UserPrincipal userPrincipal
    ) {
        return stopService.updateIsFirstAccess(userPrincipal);
    }
}
