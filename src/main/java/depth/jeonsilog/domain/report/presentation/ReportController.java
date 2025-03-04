package depth.jeonsilog.domain.report.presentation;

import depth.jeonsilog.domain.report.application.ReportService;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> report(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReportRequestDto.ReportReq reportReq
    ) {
        reportService.report(userPrincipal, reportReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
