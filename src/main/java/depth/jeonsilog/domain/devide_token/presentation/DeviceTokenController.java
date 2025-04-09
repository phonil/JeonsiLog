package depth.jeonsilog.domain.devide_token.presentation;

import depth.jeonsilog.domain.devide_token.application.DeviceTokenService;
import depth.jeonsilog.domain.devide_token.dto.DeviceTokenRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deviceTokens")
public class DeviceTokenController implements DeviceTokenApi {

    private final DeviceTokenService deviceTokenService;

    @Override
    @PostMapping
    public ResponseEntity<Void> registerToken(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody DeviceTokenRequestDto.DeviceTokenReq deviceTokenReq
    ) {
        deviceTokenService.registerToken(userPrincipal, deviceTokenReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteToken(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody DeviceTokenRequestDto.DeviceTokenReq deviceTokenReq
    ) {
        deviceTokenService.deleteToken(userPrincipal, deviceTokenReq);
        return ResponseEntity.noContent().build();
    }
}
