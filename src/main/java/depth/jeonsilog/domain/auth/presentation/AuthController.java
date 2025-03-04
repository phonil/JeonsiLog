package depth.jeonsilog.domain.auth.presentation;

import depth.jeonsilog.domain.auth.application.AuthService;
import depth.jeonsilog.domain.auth.dto.AuthRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<Void> signup(
            @Valid @RequestBody AuthRequestDto.SignUpReq signUpRequest
    ) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signin(
            @Valid @RequestBody AuthRequestDto.SignInReq signInRequest
    ) {
        return authService.signIn(signInRequest);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(
            @Valid @RequestBody AuthRequestDto.RefreshTokenReq tokenRefreshRequest
    ) {
        return authService.refresh(tokenRefreshRequest);
    }

    @DeleteMapping(value = "/sign-out")
    public ResponseEntity<Void> signOut(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        authService.signOut(userPrincipal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> checkNickname(
        @PathVariable(value = "nickname") String nickname
    ) {
        return authService.checkNickname(nickname);
    }
}
