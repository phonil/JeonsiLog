package depth.jeonsilog.domain.report.application;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.reply.application.ReplyService;
import depth.jeonsilog.domain.report.converter.ReportConverter;
import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.report.domain.ReportType;
import depth.jeonsilog.domain.report.domain.repository.ReportRepository;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.domain.review.application.ReviewService;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final UserService userService;
    private final ReviewService reviewService;
    private final ReplyService replyService;
    private final ExhibitionService exhibitionService;

    // TODO Think : Report의 reportedId는 타입 별 해당 PK(ID)임 !
    @Transactional
    public void report(UserPrincipal userPrincipal, ReportRequestDto.ReportReq reportReq) {
        User user = userService.validateUserByToken(userPrincipal);
        Long id = reportReq.getReportedId();
        ReportType reportType = reportReq.getReportType();
        validateReportOnce(user.getId(), reportReq);
        validate(reportType, id);
        Report report = ReportConverter.toReport(user, id, reportType);
        reportRepository.save(report);

        // 같은 신고 모두 찾아서 counting, isChecked 업데이트 해줘야 함.
        List<Report> reports = reportRepository.findByReportedIdAndReportType(id, reportType);
        if (reports.size() > 1) {
            reports.forEach(r -> {
                r.updateCounting(reports.get(0).getCounting() + 1);
                r.updateChecked(false);
            });
        }
    }

    private void validate(ReportType reportType, Long id) {
        switch (reportType) {
            case REVIEW -> reviewService.validateReviewById(id);
            case REPLY -> replyService.validateReplyById(id);
            case EXHIBITION -> {
                Exhibition exhibition = exhibitionService.validateExhibitionById(id);
                DefaultAssert.isTrue(exhibition.getImageUrl() == null, "이미 등록된 포스터가 존재합니다.");
            }
            case LINK -> {
                Exhibition exhibition = exhibitionService.validateExhibitionById(id);
                DefaultAssert.isTrue(exhibition.getPlace().getHomePage() == null, "이미 등록된 홈페이지 링크가 존재합니다.");
            }
            case ADDRESS -> {
                Exhibition exhibition = exhibitionService.validateExhibitionById(id);
                DefaultAssert.isTrue(exhibition.getPlace().getAddress() == null, "이미 등록된 주소가 존재합니다.");
            }
            case PHONE_NUMBER -> {
                Exhibition exhibition = exhibitionService.validateExhibitionById(id);
                DefaultAssert.isTrue(exhibition.getPlace().getTel() == null, "이미 등록된 전화번호가 존재합니다.");
            }
        }
    }

    // TODO: 같은 사용자가 같은 신고 여러 번 하면 한 번으로 처리
    public void validateReportOnce(Long userId, ReportRequestDto.ReportReq dto) {
        Optional<Report> report = reportRepository.findByUserIdAndReportTypeAndReportedId(userId, dto.getReportType(), dto.getReportedId());
        DefaultAssert.isTrue(report.isEmpty(), "이미 신고하였습니다.");
    }
}
