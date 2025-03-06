package depth.jeonsilog.domain.stop.application;

import depth.jeonsilog.domain.stop.domain.Stop;
import depth.jeonsilog.domain.stop.domain.repository.StopRepository;
import depth.jeonsilog.domain.stop.dto.StopResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StopService {

    private final StopRepository stopRepository;
    private final UserService userService;

    public ResponseEntity<?> findUserIsStop(final UserPrincipal userPrincipal) {
        User user = userService.validateUserByToken(userPrincipal);
        Optional<Stop> stop = stopRepository.findByUserId(user.getId());
        DefaultAssert.isTrue(stop.isPresent(), "유저 정지 이력이 없습니다.");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = stop.get().getCreatedDate().plusDays(7);
        Integer between = target.getDayOfMonth() - now.getDayOfMonth();

        StopResponseDto.StopUserRes dto = StopResponseDto.StopUserRes.builder()
                .reason(stop.get().getReason())
                .remainingDays(between)
                .isFirstAccess(stop.get().getIsFirstAccess())
                .build();

        ApiResponse apiResponse = ApiResponse.toApiResponse(dto);
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public void updateIsFirstAccess(final UserPrincipal userPrincipal) {
        User user = userService.validateUserByToken(userPrincipal);
        Optional<Stop> stop = stopRepository.findByUserId(user.getId());
        DefaultAssert.isTrue(stop.isPresent(), "유저 정지 이력이 없습니다.");
        stop.get().updateIsFirstAccess();
    }
}
