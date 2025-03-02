package depth.jeonsilog.domain.calendar.presentation;

import depth.jeonsilog.domain.calendar.application.CalendarService;
import depth.jeonsilog.domain.calendar.dto.CalendarRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> uploadImage(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart("uploadImageReq") CalendarRequestDto.UploadImageReq uploadImageReq,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        calendarService.uploadImage(userPrincipal, uploadImageReq, img);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/exhibition")
    public ResponseEntity<Void> uploadPoster(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody CalendarRequestDto.UploadPosterReq uploadPosterReq
    ) {
        calendarService.uploadPoster(userPrincipal, uploadPosterReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCalendar(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody CalendarRequestDto.UploadImageReq deleteImageReq
    ) {
        calendarService.deleteCalendar(userPrincipal, deleteImageReq);
        return ResponseEntity.noContent().build();
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
