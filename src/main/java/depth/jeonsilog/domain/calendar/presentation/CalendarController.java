package depth.jeonsilog.domain.calendar.presentation;

import depth.jeonsilog.domain.calendar.application.CalendarService;
import depth.jeonsilog.domain.calendar.dto.CalendarRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/calendars")
public class CalendarController implements CalendarApi {

    private final CalendarService calendarService;

    @PostMapping(value = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadImage(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart("uploadImageReq") CalendarRequestDto.UploadImageReq uploadImageReq,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        return calendarService.uploadImage(userPrincipal, uploadImageReq, img);
    }

    @PostMapping(value = "/exhibition")
    public ResponseEntity<?> uploadPoster(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody CalendarRequestDto.UploadPosterReq uploadPosterReq
    ) {
        return calendarService.uploadPoster(userPrincipal, uploadPosterReq);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCalendar(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody CalendarRequestDto.UploadImageReq deleteImageReq
    ) {
        return calendarService.deleteCalendar(userPrincipal, deleteImageReq);
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> deleteCalendar(
           @CurrentUser UserPrincipal userPrincipal,
            @PathVariable String date
    ) {
        return calendarService.getMyPhotoCalendar(userPrincipal, date);
    }

    @GetMapping("/{userId}/{date}")
    public ResponseEntity<?> deleteCalendar(
            @PathVariable Long userId,
            @PathVariable String date
    ) {
        return calendarService.getUserPhotoCalendar(userId, date);
    }
}
