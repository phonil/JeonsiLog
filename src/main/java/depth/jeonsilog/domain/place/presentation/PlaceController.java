package depth.jeonsilog.domain.place.presentation;

import depth.jeonsilog.domain.place.application.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/places")
public class PlaceController implements PlaceApi {

    private final PlaceService placeService;

    @GetMapping("/exhibition/{placeId}")
    public ResponseEntity<?> findExhibitionListInPlace(
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "placeId") Long placeId
    ) {
        return placeService.findExhibitionListInPlace(page, placeId);
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchPlaces(
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "searchWord") String searchWord
    ) {
        return placeService.searchPlaces(page, searchWord);
    }
}
