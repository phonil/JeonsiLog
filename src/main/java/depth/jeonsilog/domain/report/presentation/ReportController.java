package depth.jeonsilog.domain.report.presentation;

import depth.jeonsilog.domain.report.application.ReportService;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> report(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReportRequestDto.ReportReq reportReq
    ) {
        return reportService.report(userPrincipal, reportReq);
    }
}
