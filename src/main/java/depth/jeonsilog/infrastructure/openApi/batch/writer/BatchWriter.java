package depth.jeonsilog.infrastructure.openApi.batch.writer;

import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.global.aop.BatchLog;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BatchWriter {

    private final PlaceRepository placeRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @BatchLog
    @MethodTimer
    @Transactional
    public List<Integer> writePlace(List<PlaceDtoToWrite> placeDtoListToWrite) {
        List<Integer> placeSeqList = new ArrayList<>();
        placeRepository.bulkUpsertJdbcTemplate(placeDtoListToWrite);
        for (PlaceDtoToWrite placeDtoToWrite : placeDtoListToWrite) {
            logger.info("## Writer ## [Write Place Seq(Upsert)], {}", placeDtoToWrite.getSeq());
            placeSeqList.add(placeDtoToWrite.getSeq());
        }
        return placeSeqList;
    }

    @BatchLog
    @MethodTimer
    @Transactional
    public void writeExhibition(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite, List<Integer> placeSeqList) {
        List<Place> placeList = placeRepository.findBySeqIn(placeSeqList);
        Map<Integer, Long> placeIdMap = placeList.stream()
                .collect(Collectors.toMap(Place::getSeq, Place::getId));
        for (ExhibitionDtoToWrite exhibitionDtoToWrite : exhibitionDtoListToWrite) {
            logger.info("## Writer ## [Write Exhibition Seq(Upsert)], {}", exhibitionDtoToWrite.getExhibitionSeq());
            Long placeId = placeIdMap.get(exhibitionDtoToWrite.getPlaceSeq());
            exhibitionDtoToWrite.setPlaceId(placeId);
        }
        exhibitionRepository.bulkUpsertJdbcTemplate(exhibitionDtoListToWrite);
    }
}
