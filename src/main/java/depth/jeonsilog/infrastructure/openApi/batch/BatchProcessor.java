package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.write.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.write.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.ProcessExhibitionDetailConstant.*;

@RequiredArgsConstructor
@Component
public class BatchProcessor {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<PlaceDtoToWrite> processPlace(List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList) {
        List<PlaceDtoToWrite> placeDtoListToWrite = new ArrayList<>();
        for (PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo placeInfo : placeInfoList) {
            String tel = placeInfo.getCulTel();
            if (tel != null) {
                if (tel.equals("") || tel.equals(" ")) {
                    placeInfo.setCulTel(null);
                }
                else if (tel.contains(",") || tel.contains("~")) {
                    tel = selectOneTel(tel);
                    placeInfo.setCulTel(tel);
                }
            }
            String homepage = placeInfo.getCulHomeUrl();
            if (homepage != null) {
                if (homepage.equals("") || homepage.equals(" "))
                    placeInfo.setCulHomeUrl(null);
            }

            PlaceDtoToWrite placeDtoToWrite = PlaceDtoToWrite.builder()
                    .seq(placeInfo.getSeq())
                    .name(placeInfo.getCulName())
                    .address(placeInfo.getCulAddr())
                    .tel(placeInfo.getCulTel())
                    .homePage(placeInfo.getCulHomeUrl())
                    .build();
            placeDtoListToWrite.add(placeDtoToWrite);
        }
        return placeDtoListToWrite;
    }

    public List<ExhibitionDtoToWrite> processExhibition(List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList) {
        List<ExhibitionDtoToWrite> exhibitionDtoListToWrite = new ArrayList<>();
        for (ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo : performanceInfoList) {
            String name = performanceInfo.getTitle();
            if (name == null || name.equals("") || name.equals(" "))
                performanceInfoList.remove(performanceInfo);
            String changedName = changeName(name);
            performanceInfo.setTitle(changedName);

            PriceKeyword priceKeyword = null;
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

            ExhibitionDtoToWrite exhibitionDtoToWrite = ExhibitionDtoToWrite.builder()
                    .placeSeq(performanceInfo.getPlaceSeq())
                    .name(performanceInfo.getTitle())
                    .imageUrl(performanceInfo.getImgUrl())
                    .operatingKeyword(operatingKeyword)
                    .priceKeyword(priceKeyword)
                    .price(performanceInfo.getPrice())
                    .startDate(performanceInfo.getStartDate())
                    .endDate(performanceInfo.getEndDate())
                    .exhibitionSeq(performanceInfo.getSeq())
                    .build();
            exhibitionDtoListToWrite.add(exhibitionDtoToWrite);
        }
        return exhibitionDtoListToWrite;
    }

    private String selectOneTel(String tel) {
        String result = tel;
        if (result.contains(","))
            result = result.substring(0, tel.indexOf(",")).trim();
        else if (tel.contains("~"))
            result = result.substring(0, tel.indexOf("~")).trim();
        return result;
    }

    private String changeName(String name) {
        String changedName = name;
        if (changedName.contains(AMP))
            changedName = changedName.replace(AMP, REPLACE_AMP);
        if (changedName.contains(LT))
            changedName = changedName.replace(LT, REPLACE_LT);
        if (changedName.contains(GT))
            changedName = changedName.replace(GT, REPLACE_GT);
        if (changedName.contains(QUOT))
            changedName = changedName.replace(QUOT, REPLACE_QUOT);
        if (changedName.contains(BACKTICK))
            changedName = changedName.replace(BACKTICK, REPLACE_BACKTICK);
        return changedName;
    }
}
