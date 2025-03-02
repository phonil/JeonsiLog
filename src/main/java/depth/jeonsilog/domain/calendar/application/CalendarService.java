package depth.jeonsilog.domain.calendar.application;

import depth.jeonsilog.domain.calendar.converter.CalendarConverter;
import depth.jeonsilog.domain.calendar.domain.Calendar;
import depth.jeonsilog.domain.calendar.domain.repository.CalendarRepository;
import depth.jeonsilog.domain.calendar.dto.CalendarRequestDto;
import depth.jeonsilog.domain.calendar.dto.CalendarResponseDto;
import depth.jeonsilog.domain.s3.application.S3Uploader;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final UserService userService;
    private final S3Uploader s3Uploader;

    private static final String DIRNAME = "calendar_img";

    @Transactional
    public void uploadImage(UserPrincipal userPrincipal, CalendarRequestDto.UploadImageReq uploadImageReq, MultipartFile img) throws IOException {
        User findUser = userService.validateUserByToken(userPrincipal);

        if (!img.isEmpty()) {
            String storedFileName = s3Uploader.upload(img, DIRNAME);

            Optional<Calendar> checkCalendar = calendarRepository.findByUserAndPhotoDate(findUser, uploadImageReq.getDate());
            if (checkCalendar.isPresent()) {
                Calendar calendar = checkCalendar.get();
                s3Uploader.deleteImage(DIRNAME, calendar.getImageUrl());
                calendar.updateImage(storedFileName);
            } else {
                Calendar calendar = CalendarConverter.toCalendar(findUser, uploadImageReq.getDate(), storedFileName, uploadImageReq.getCaption());
                calendarRepository.save(calendar);
            }
        }
    }

    @Transactional
    public void uploadPoster(UserPrincipal userPrincipal, CalendarRequestDto.UploadPosterReq uploadPosterReq) {
        User findUser = userService.validateUserByToken(userPrincipal);

        Optional<Calendar> checkCalendar = calendarRepository.findByUserAndPhotoDate(findUser, uploadPosterReq.getDate());
        if (checkCalendar.isPresent()) {
            checkCalendar.get().updateImage(uploadPosterReq.getImgUrl());
        } else {
            Calendar calendar = CalendarConverter.toCalendar(findUser, uploadPosterReq.getDate(), uploadPosterReq.getImgUrl(), uploadPosterReq.getCaption());
            calendarRepository.save(calendar);
        }
    }

    @Transactional
    public void deleteCalendar(UserPrincipal userPrincipal, CalendarRequestDto.UploadImageReq deleteImageReq) {
        User findUser = userService.validateUserByToken(userPrincipal);

        Optional<Calendar> findCalendar = calendarRepository.findByUserAndPhotoDate(findUser, deleteImageReq.getDate());
        DefaultAssert.isTrue(findCalendar.isPresent(), "해당 날짜에 이미지가 없습니다.");

        Calendar calendar = findCalendar.get();

        s3Uploader.deleteImage(DIRNAME, calendar.getImageUrl());
        calendarRepository.delete(calendar);
    }

    // Description: 나의 월별 이미지 목록 조회
    public ResponseEntity<?> getMyPhotoCalendar(UserPrincipal userPrincipal, String yearMonth) {
        User findUser = userService.validateUserByToken(userPrincipal);

        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, 6));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, 1)
                .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
        int lastDay = lastDayOfMonth.getDayOfMonth();
        LocalDate endDate = LocalDate.of(year, month, lastDay);

        List<Calendar> calendarList = calendarRepository.findAllByUserAndPhotoDateBetween(findUser, startDate, endDate);

        List<CalendarResponseDto.ImageRes> calendarListRes = CalendarConverter.toImageRes(calendarList);

        calendarListRes.sort(Comparator.comparing(CalendarResponseDto.ImageRes::getDate));

        ApiResponse apiResponse = ApiResponse.toApiResponse(calendarListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // Description: 타 유저의 월별 이미지 목록 조회
    public ResponseEntity<?> getUserPhotoCalendar(Long userId, String yearMonth) {
        User findUser = userService.validateUserById(userId);

        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, 6));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, 1)
                .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
        int lastDay = lastDayOfMonth.getDayOfMonth();
        LocalDate endDate = LocalDate.of(year, month, lastDay);

        List<Calendar> calendarList = calendarRepository.findAllByUserAndPhotoDateBetween(findUser, startDate, endDate);

        List<CalendarResponseDto.ImageRes> calendarListRes = CalendarConverter.toImageRes(calendarList);

        calendarListRes.sort(Comparator.comparing(CalendarResponseDto.ImageRes::getDate));

        ApiResponse apiResponse = ApiResponse.toApiResponse(calendarListRes);
        return ResponseEntity.ok(apiResponse);
    }
}
