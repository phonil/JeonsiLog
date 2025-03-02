package depth.jeonsilog.domain.report.application;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.reply.domain.repository.ReplyRepository;
import depth.jeonsilog.domain.report.converter.ReportConverter;
import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.report.domain.repository.ReportRepository;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.domain.report.dto.ReportResponseDto;
import depth.jeonsilog.domain.review.domain.repository.ReviewRepository;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminReportService {

    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final ReportRepository reportRepository;

    private final UserService userService;
    private final ExhibitionService exhibitionService;

    private static final int DEFAULT_PAGE_SIZE = 20; // 페이지당 기본 20개 항목

    public ResponseEntity<?> findReportList(Integer page, UserPrincipal userPrincipal) {

        User user = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(user.getRole().equals(Role.ADMIN), "관리자만 조회할 수 있습니다.");

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<Report> response = reportRepository.findReportsWithSortingAndCounting(pageable);
        List<Report> reports = response.getContent();

        DefaultAssert.isTrue(!reports.isEmpty(), "신고 내역이 존재하지 않습니다.");

        List<Object> targetList = createTargetList(reports);

        List<ReportResponseDto.ReportRes> reportResList = ReportConverter.toReportResList(reports, targetList);
        boolean hasNextPage = response.hasNext();
        ReportResponseDto.ReportResListWithPaging reportResListWithPaging = ReportConverter.toReportResListWithPaging(hasNextPage, reportResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reportResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> checkReport(UserPrincipal userPrincipal, ReportRequestDto.ReportReq dto) {

        User user = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(user.getRole().equals(Role.ADMIN), "관리자만 확인할 수 있습니다.");

        List<Report> reports = reportRepository.findByReportedIdAndReportType(dto.getReportedId(), dto.getReportType());
        reports.forEach(r -> {
            r.updateChecked(true);
        });

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("신고가 확인되었습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    private List<Object> createTargetList(List<Report> reports) {
        List<Object> targetList = new ArrayList<>();

        for (Report report : reports) {
            Object target = new Object();

            switch (report.getReportType()) {
                case REVIEW -> target = reviewRepository.findReviewByReviewId(report.getReportedId());
                case REPLY -> target = replyRepository.findReplyByReplyId(report.getReportedId());
                default -> target = exhibitionService.validateExhibitionById(report.getReportedId());
            }
            targetList.add(target);
        }
        return targetList;
    }
}
