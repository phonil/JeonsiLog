package depth.jeonsilog.domain.report.presentation;

import depth.jeonsilog.domain.report.application.AdminReportService;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController implements AdminReportApi {

    private final AdminReportService adminReportService;

    @GetMapping
    public ResponseEntity<?> findReportList(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page") Integer page
    ) {
        return adminReportService.findReportList(page, userPrincipal);
    }

    @PatchMapping("/check")
    public ResponseEntity<Void> checkReport(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReportRequestDto.ReportReq reportReq
    ) {
        adminReportService.checkReport(userPrincipal, reportReq);
        return ResponseEntity.noContent().build();
    }
}
