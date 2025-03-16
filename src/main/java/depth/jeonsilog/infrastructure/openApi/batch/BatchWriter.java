package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.infrastructure.openApi.batch.write.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.write.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BatchWriter {

    private final PlaceRepository placeRepository;
    private final ExhibitionRepository exhibitionRepository;

    public void writePlace(List<PlaceDtoToWrite> placeDtoListToWrite) {
        // 1) Update 대상 vs Insert 대상 분리(혹은 Upsert)
        for (PlaceDtoToWrite placeDtoToWrite : placeDtoListToWrite) {
            Optional<Place> existingPlace = placeRepository.findBySeq(placeDtoToWrite.getSeq());
            if (existingPlace.isPresent())
                updatePlace(existingPlace.get(), placeDtoToWrite);
            else
                insertPlace(placeDtoToWrite);
        }
    }

    private void updatePlace(Place place, PlaceDtoToWrite dto) {
        place.updateTel(dto.getTel());        // 엔티티에 updateTel() 구현
        place.updateHomepage(dto.getHomePage());
        // ... address나 name도 바뀌었으면 반영
    }

    private void insertPlace(PlaceDtoToWrite dto) {
        Place newPlace = Place.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .tel(dto.getTel())
                .homePage(dto.getHomePage())
                .build();
        placeRepository.save(newPlace);
    }

    public void writeExhibition(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite) {
        for (ExhibitionDtoToWrite exhibitionDtoToWrite : exhibitionDtoListToWrite) {
            Optional<Exhibition> existingExhibition = exhibitionRepository.findByExhibitionSeq(exhibitionDtoToWrite.getExhibitionSeq());
            if (existingExhibition.isPresent())
                updateExhibition(existingExhibition.get(), exhibitionDtoToWrite);
            else
                insertExhibition(exhibitionDtoToWrite);
        }
    }

    private void updateExhibition(Exhibition ex, ExhibitionDtoToWrite dto) {
        ex.updateName(dto.getName());
        ex.updateOperatingKeyword(dto.getOperatingKeyword());
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
}
