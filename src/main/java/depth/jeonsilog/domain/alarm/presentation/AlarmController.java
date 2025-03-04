package depth.jeonsilog.domain.alarm.presentation;


import depth.jeonsilog.domain.alarm.application.AlarmService;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController implements AlarmApi {

    private final AlarmService alarmService;

    @GetMapping("/activity")
    public ResponseEntity<?> getActivityAlarmList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return alarmService.getActivityAlarmList(page, userPrincipal);
    }

    @GetMapping("/exhibition")
    public ResponseEntity<?> getExhibitionAlarmList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return alarmService.getExhibitionAlarmList(page, userPrincipal);
    }

    @PatchMapping("/check/{alarmId}")
    public ResponseEntity<?> checkReport(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long alarmId
    ) {
        return alarmService.checkAlarm(userPrincipal, alarmId);
    }
}
