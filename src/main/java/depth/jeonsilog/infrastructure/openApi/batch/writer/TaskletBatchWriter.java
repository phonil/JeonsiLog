package depth.jeonsilog.infrastructure.openApi.batch.writer;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskletBatchWriter {

    private final PlaceRepository placeRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    public void writePlace(List<PlaceDtoToWrite> placeDtoListToWrite) {
        for (PlaceDtoToWrite placeDtoToWrite : placeDtoListToWrite) {
            Optional<Place> existingPlace = placeRepository.findBySeq(placeDtoToWrite.getSeq());
            logger.info("## Writer ## [Exist Place? (boolean)], {}", existingPlace.isPresent());
            placeRepository.findBySeq(placeDtoToWrite.getSeq())
                    .ifPresentOrElse(
                            place -> updatePlace(place, placeDtoToWrite),
                            () -> insertPlace(placeDtoToWrite)
                    );
        }
    }

    private void updatePlace(Place place, PlaceDtoToWrite dto) {
        place.updateTel(dto.getTel());        // 엔티티에 updateTel() 구현
        place.updateHomepage(dto.getHomePage());
        // ... address나 name도 바뀌었으면 반영
    }

    private void insertPlace(PlaceDtoToWrite dto) {
        Place newPlace = Place.builder()
                .seq(dto.getSeq())
                .name(dto.getName())
                .address(dto.getAddress())
                .tel(dto.getTel())
                .homePage(dto.getHomePage())
                .build();
        placeRepository.save(newPlace);
    }

    @MethodTimer
    public void writeExhibition(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite) {
        for (ExhibitionDtoToWrite exhibitionDtoToWrite : exhibitionDtoListToWrite) {
            Optional<Exhibition> existingExhibition = exhibitionRepository.findByExhibitionSeq(exhibitionDtoToWrite.getExhibitionSeq());
            logger.info("## Writer ## [Exist Exhibition ? (boolean)], {}", existingExhibition.isPresent());
            exhibitionRepository.findByExhibitionSeq(exhibitionDtoToWrite.getExhibitionSeq())
                    .ifPresentOrElse(
                            exhibition -> updateExhibition(exhibition, exhibitionDtoToWrite),
                            () -> insertExhibition(exhibitionDtoToWrite)
                    );
        }
    }

    private void updateExhibition(Exhibition exhibition, ExhibitionDtoToWrite dto) {
        exhibition.updateName(dto.getName());
        exhibition.updateOperatingKeyword(dto.getOperatingKeyword());
        // etc...
    }

    private void insertExhibition(ExhibitionDtoToWrite exhibitionDtoToWrite) {
        // placeSeq 이용해 Place를 찾거나 null 처리
        Optional<Place> findPlace = placeRepository.findBySeq(exhibitionDtoToWrite.getPlaceSeq());
        Place place = null;
        if (findPlace.isPresent())
            place = findPlace.get();

        Exhibition newExhibition = Exhibition.builder()
                .place(place)
                .name(exhibitionDtoToWrite.getName())
                .imageUrl(exhibitionDtoToWrite.getImageUrl())
                .operatingKeyword(exhibitionDtoToWrite.getOperatingKeyword())
                .priceKeyword(exhibitionDtoToWrite.getPriceKeyword())
                .price(exhibitionDtoToWrite.getPrice())
                .startDate(exhibitionDtoToWrite.getStartDate())
                .endDate(exhibitionDtoToWrite.getEndDate())
                .exhibitionSeq(exhibitionDtoToWrite.getExhibitionSeq())
                .build();
        exhibitionRepository.save(newExhibition);
    }

    @MethodTimer
    @Transactional
    public List<Integer> writePlaceBulk(List<PlaceDtoToWrite> placeDtoListToWrite) {
        List<Integer> placeSeqList = new ArrayList<>();
        placeRepository.bulkUpsertJdbcTemplate(placeDtoListToWrite);
        for (PlaceDtoToWrite placeDtoToWrite : placeDtoListToWrite) {
            logger.info("## Writer ## [Write Place Seq(Upsert)], {}", placeDtoToWrite.getSeq());
            placeSeqList.add(placeDtoToWrite.getSeq());
        }
        return placeSeqList;
    }

    @MethodTimer
    @Transactional
    public void writeExhibitionBulk(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite, List<Integer> placeSeqList) {
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
