package depth.jeonsilog.domain.stop.presentation;

import depth.jeonsilog.domain.stop.application.AdminStopService;
import depth.jeonsilog.domain.stop.dto.StopRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stop")
public class AdminStopController implements AdminStopApi {

    private final AdminStopService adminStopService;

    @PostMapping
    public ResponseEntity<?> stopUser(
            @CurrentUser final UserPrincipal userPrincipal,
            @Valid @RequestBody final StopRequestDto.StopUserReq dto
    ) {
        return adminStopService.stopUser(userPrincipal, dto);
    }
}
