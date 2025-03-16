package depth.jeonsilog.infrastructure.openApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.infrastructure.openApi.application.ModifyService;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionListDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BatchService {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceRepository placeRepository;

    private final OpenApiReader openApiReader;
    private final ObjectMapper objectMapper;

    private final ModifyService modifyService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public ResponseEntity<ApiResponse> saveBatch() throws JsonProcessingException, IOException {
        String fromDate = LocalDate.now().minusMonths(3).format(formatter);
        String toDate = LocalDate.now().plusMonths(1).format(formatter);

        // TODO : 횟수 -> 토큰버킷 이런거?
        Integer page = 1;
        while (true) {
            // Description : Exhibition
            String exhibitionListXml = openApiReader.callExhibitionListApi(fromDate, toDate, page, 100);
            String exhibitionListJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionListXml);
            ExhibitionListDTO exhibitionList = objectMapper.readValue(exhibitionListJsonStr, ExhibitionListDTO.class);

            ArrayList<ExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListMsgBodyDTO.PerformElement> performanceList = exhibitionList.getResponse().getMsgBody().getPerforList();
            // Description : Exhibition Details - 전시회 목록의 해당 페이지의 전시회들 상세 호출 ( 한 페이지에 최대 100개 설정 )
            for (int i = 0; i < performanceList.size(); i++) {
                String exhibitionDetailXml = openApiReader.callExhibitionDetailApi(exhibitionList.getResponse().getMsgBody().getPerforList().get(i).getSeq());
                String exhibitionDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionDetailXml);
                ExhibitionDetailDTO exhibitionDetail = objectMapper.readValue(exhibitionDetailJsonStr, ExhibitionDetailDTO.class);


                // TODO : Place of ExhibitionDetail
                String placeDetailXml = openApiReader.callPlaceDetailApi(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPlaceSeq());
                String placeDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(placeDetailXml);
                PlaceDetailDTO placeDetail = objectMapper.readValue(placeDetailJsonStr, PlaceDetailDTO.class);
                if (placeDetail.getResponse().getMsgBody().getSeq() == 0)
                    placeDetail.getResponse().getMsgBody().setPlaceInfo(new PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo());


                // Description : place 있는 지 확인
                PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo placeInfo = placeDetail.getResponse().getMsgBody().getPlaceInfo();
                String placeName = placeInfo.getCulName();
                String placeAddr = placeInfo.getCulAddr();
                Optional<Place> placeByName = Optional.empty();
                Optional<Place> placeByAddr = Optional.empty();
                if (placeName != null)
                    placeByName = placeRepository.findByName(placeName);
                if (placeAddr != null)
                    placeByAddr = placeRepository.findByAddress(placeAddr);
                Place place = null;
                // If : 만약 해당 전시 공간 이름 혹은 전시 공간 주소가 같은 것이 있다면 새로이 저장 x / 기존 거 사용
                if (placeByName.isPresent()) {
                    place = placeByName.get();
                } else if (placeByAddr.isPresent()) {
                    place = placeByAddr.get();
                } else {
                    if (placeName == null || placeAddr == null) {
                        place = Place.builder()
                                .name(null)
                                .address(null)
                                .homePage(null)
                                .tel(null)
                                .build();
                        placeRepository.save(place);
                    } else {
                        // Place 최초 저장
                        place = Place.builder()
                                .name(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulName())
                                .address(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulAddr())
                                .homePage(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulHomeUrl())
                                .tel(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulTel())
                                .build();
                        placeRepository.save(place);
                    }
                }

                // Description : Exhibition 저장
                // Description : 유료 / 무료 키워드
                PriceKeyword priceKeyword = null;
                ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo = exhibitionDetail.getResponse().getMsgBody().getPerforInfo();
                if (performanceInfo.getPrice().equals("무료") || performanceInfo.getPrice().equals("전석무료"))
                    priceKeyword = PriceKeyword.FREE;
                else
                    priceKeyword = PriceKeyword.PAY;

                LocalDate now = LocalDate.now();
                LocalDate startDate = LocalDate.parse(performanceInfo.getStartDate(), formatter);
                LocalDate endDate = LocalDate.parse(performanceInfo.getEndDate(), formatter);

                OperatingKeyword operatingKeyword = null;
                if ((now.isEqual(startDate) || now.isAfter(startDate)) && (now.isEqual(endDate) || now.isBefore(endDate)))
                    operatingKeyword = OperatingKeyword.ON_DISPLAY;
                else if (now.isBefore(startDate))
                    operatingKeyword = OperatingKeyword.BEFORE_DISPLAY;
                else
                    operatingKeyword = OperatingKeyword.AFTER_DISPLAY;

                Exhibition exhibition = Exhibition.builder()
                        .place(place)
                        .name(performanceInfo.getTitle())
                        .operatingKeyword(operatingKeyword)
                        .priceKeyword(priceKeyword)
                        .price(performanceInfo.getPrice())
                        .startDate(performanceInfo.getStartDate())
                        .endDate(performanceInfo.getEndDate())
                        .information(null)
                        .rate(null)
                        .exhibitionSeq(performanceInfo.getSeq())
                        .imageUrl(performanceInfo.getImgUrl())
                        .build();
                exhibitionRepository.save(exhibition);
            }

            // TODO : while문 종료조건 : 마지막 page까지 호출
            Integer totalCount = exhibitionList.getResponse().getMsgBody().getTotalCount();
            Integer rows = exhibitionList.getResponse().getMsgBody().getRows();
            Integer numOfPages = (totalCount / rows) + 1;
            if (page == numOfPages) { // 페이지 조절

                modifyService.modifyExhibitionName();
                modifyService.modifyPlaceTel();
                modifyService.modifyPlaceHomepage();

                ApiResponse apiResponse = ApiResponse.builder()
                        .check(true)
                        .information(exhibitionList)
                        .build();
                return ResponseEntity.ok(apiResponse);
            }
            page++;
        }

    }
}
