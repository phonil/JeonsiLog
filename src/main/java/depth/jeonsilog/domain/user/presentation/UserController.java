package depth.jeonsilog.domain.user.presentation;

import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.dto.UserRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi{

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findUserByToken(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUserByToken(userPrincipal);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findUser(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "userId") Long userId
    ) {
        return userService.findUserById(userPrincipal, userId);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> changeNickname(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UserRequestDto.ChangeNicknameReq changeNicknameReq
    ) {
        userService.changeNickname(userPrincipal, changeNicknameReq);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> changeProfile(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        userService.changeProfile(userPrincipal, img);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchUsers(
            @CurrentUser UserPrincipal userPrincipal,
            Integer page,
            @PathVariable(value = "searchWord") String keyword
    ) {
        return userService.searchUsers(page, userPrincipal, keyword);
    }

    @PatchMapping("/calendar")
    public ResponseEntity<?> switchIsOpen(
           @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsOpen(userPrincipal);
    }

    @PatchMapping("/alarm-exhibition")
    public ResponseEntity<?> switchIsRecvExhibition(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsRecvExhibition(userPrincipal);
    }

    @PatchMapping("/alarm-active")
    public ResponseEntity<?> switchIsRecvActive(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsRecvActive(userPrincipal);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        userService.deleteUser(userPrincipal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/calendar/{userId}")
    public ResponseEntity<?> getIsOpen(
            @PathVariable Long userId
    ) {
        return userService.getIsOpen(userId);
    }

    @GetMapping("/reception")
    public ResponseEntity<?> getRecvOrNot(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.getRecvOrNot(userPrincipal);
    }


    @PatchMapping("/fcm/token")
    public ResponseEntity<Void> updateFcmToken(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody UserRequestDto.UpdateFcmToken fcmToken
            ) {
        userService.updateFcmToken(userPrincipal, fcmToken);
        return ResponseEntity.noContent().build();
    }
}
