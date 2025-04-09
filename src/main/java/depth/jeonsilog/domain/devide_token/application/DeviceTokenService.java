package depth.jeonsilog.domain.devide_token.application;

import depth.jeonsilog.domain.devide_token.domain.DeviceToken;
import depth.jeonsilog.domain.devide_token.domain.repository.DeviceTokenRepository;
import depth.jeonsilog.domain.devide_token.dto.DeviceTokenRequestDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeviceTokenService {

    private final UserService userService;
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public void registerToken(UserPrincipal userPrincipal, DeviceTokenRequestDto.DeviceTokenReq deviceTokenReq) {
        User user = userService.validateUserByToken(userPrincipal);
        String token = deviceTokenReq.getDeviceToken();
        deviceTokenRepository.findByDeviceToken(token)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateUser(user),
                        () -> {
                            DeviceToken deviceToken = DeviceToken.builder()
                                    .user(user)
                                    .deviceToken(token)
                                    .build();
                            deviceTokenRepository.save(deviceToken);
                        }
                );
    }

    @Transactional
    public void deleteToken(UserPrincipal userPrincipal, DeviceTokenRequestDto.DeviceTokenReq deviceTokenReq) {
        User user = userService.validateUserByToken(userPrincipal);
        deviceTokenRepository.deleteByDeviceToken(deviceTokenReq.getDeviceToken());
    }
}
