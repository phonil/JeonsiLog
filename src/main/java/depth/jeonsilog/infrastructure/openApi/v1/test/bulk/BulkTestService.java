package depth.jeonsilog.infrastructure.openApi.v1.test.bulk;

import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BulkTestService {

    private final PlaceRepository placeRepository;
    private final ExhibitionRepository exhibitionRepository;

    public void bulkUpsertPlace(List<PlaceDtoToWrite> placeDtoListToWrite) {
        placeRepository.bulkUpsertJdbcTemplate(placeDtoListToWrite);
    }

    public void bulkUpsertExhibition(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite) {
        exhibitionRepository.bulkUpsertJdbcTemplate(exhibitionDtoListToWrite);
    }
}