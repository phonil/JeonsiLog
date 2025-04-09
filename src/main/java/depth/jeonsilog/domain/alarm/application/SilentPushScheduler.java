package depth.jeonsilog.domain.alarm.application;

import depth.jeonsilog.domain.devide_token.domain.DeviceToken;
import depth.jeonsilog.domain.devide_token.domain.repository.DeviceTokenRepository;
import depth.jeonsilog.infrastructure.fcm.application.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SilentPushScheduler {

    private final DeviceTokenRepository deviceTokenRepository;
    private final FcmService fcmService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendSilentCheckPush() {
        List<DeviceToken> tokenList = deviceTokenRepository.findAll();
        int total = tokenList.size();
        tokenList.stream()
                .map(DeviceToken::getDeviceToken)
                .forEach(fcmService::sendSilentCheck);
    }
}
