package depth.jeonsilog.infrastructure.openApi.test;

import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BulkTestController {

    private final BulkTestService bulkTestService;

    @PostMapping("/place/bulk/test/upsert")
    public String testPlaceBulkUpsert(@RequestBody List<PlaceDtoToWrite> placeDtoListToWrite) {
        bulkTestService.bulkUpsertPlace(placeDtoListToWrite);
        return "Finish";
    }

    @PostMapping("/exhibition/bulk/test/upsert")
    public String testExhibitionBulkUpsert(@RequestBody List<ExhibitionDtoToWrite> exhibitionDtoToWriteList) {
        bulkTestService.bulkUpsertExhibition(exhibitionDtoToWriteList);
        return "Finish";
    }
}
