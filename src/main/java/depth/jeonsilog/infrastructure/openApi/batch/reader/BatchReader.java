package depth.jeonsilog.infrastructure.openApi.batch.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import depth.jeonsilog.global.aop.BatchLog;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.ChangedOpenApiCaller;
import depth.jeonsilog.infrastructure.openApi.DataTypeTransferUtil;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedExhibitionListDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedPlaceDetailDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BatchReader {

    private final ChangedOpenApiCaller changedOpenApiCaller;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @BatchLog
    @MethodTimer
    public List<Integer> readExhibitionList() throws IOException {
        String fromDate = "20250316";
        String toDate = "20250330";
//        String fromDate = LocalDate.now().minusMonths(3).format(formatter);
//        String toDate = LocalDate.now().plusMonths(1).format(formatter);
        Integer page = 1;
        int rows = 100;
        String exhibitionListXml = changedOpenApiCaller.callExhibitionListApi(fromDate, toDate, page, rows);
        String exhibitionListJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionListXml);
        ChangedExhibitionListDTO exhibitionList = objectMapper.readValue(exhibitionListJsonStr, ChangedExhibitionListDTO.class);
        Integer totalCount = exhibitionList.getResponse().getBody().getTotalCount();
        Integer numOfrows = exhibitionList.getResponse().getBody().getNumOfrows();
        int pageCount = (totalCount / numOfrows) + 1;
        if (totalCount % numOfrows == 0)
            pageCount--;

        List<Integer> performanceSeqList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            logger.info("## Reader ## [Exhibition List Page], {}", page);
            exhibitionListXml = changedOpenApiCaller.callExhibitionListApi(fromDate, toDate, page, rows);
            logger.info("## Reader ## [Exhibition List Function Call Return ( XML String )], {}", exhibitionListXml);
            exhibitionListJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionListXml);
            exhibitionList = objectMapper.readValue(exhibitionListJsonStr, ChangedExhibitionListDTO.class);
            List<ChangedExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListBodyDTO.Items.Item> itemList = exhibitionList.getResponse().getBody().getItems().getItem();
            for (ChangedExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListBodyDTO.Items.Item item : itemList) {
                if (!performanceSeqList.contains(item.getSeq()))
                    performanceSeqList.add(item.getSeq());
            }
            page++;
        }
        return performanceSeqList;
    }

    @BatchLog
    @MethodTimer
    public List<ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item> readExhibitionDetail(List<Integer> exhibitionSeqList) throws IOException {
//        RateLimiter limiter = RateLimiter.create(3.0);
        List<ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item> performanceInfoList = new ArrayList<>();
        int i = 1;
        for (Integer exhibitionSeq : exhibitionSeqList) {
//            limiter.acquire();
            String exhibitionDetailXml = changedOpenApiCaller.callExhibitionDetailApi(exhibitionSeq);
            logger.info("## Reader ## [Exhibition Detail Function Call Return ( XML String )], {}", exhibitionDetailXml + i++);
            String exhibitionDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionDetailXml);
            ChangedExhibitionDetailDTO exhibitionDetail = objectMapper.readValue(exhibitionDetailJsonStr, ChangedExhibitionDetailDTO.class);
            if (exhibitionDetail.getResponse().getBody().getItems() == null || exhibitionDetail.getResponse().getBody().getItems().getItem().isEmpty())
                continue;
            ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item item = exhibitionDetail.getResponse().getBody().getItems().getItem().get(0);
            performanceInfoList.add(item);
        }
        return performanceInfoList;
    }

    @BatchLog
    @MethodTimer
    public List<ChangedPlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailBodyDTO.Items.Item> readPlaceDetail(List<ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item> performanceInfoList) throws IOException {
//        RateLimiter limiter = RateLimiter.create(3.0);
        List<ChangedPlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailBodyDTO.Items.Item> placeInfoList = new ArrayList<>();
        int i = 1;
        for (ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item performanceInfo : performanceInfoList) {
            Integer placeSeq = performanceInfo.getPlaceSeq();
            if (placeSeq == 0)
                continue;
//            limiter.acquire();
            String placeDetailXml = changedOpenApiCaller.callPlaceDetailApi(performanceInfo.getPlaceSeq());
            logger.info("## Reader ## [Place Detail Function Call Return ( XML String )], {}", placeDetailXml + i++);
            String placeDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(placeDetailXml);
            ChangedPlaceDetailDTO placeDetail = objectMapper.readValue(placeDetailJsonStr, ChangedPlaceDetailDTO.class);
            if (placeDetail.getResponse().getBody().getItems() == null || placeDetail.getResponse().getBody().getItems().getItem().isEmpty())
                continue;
            ChangedPlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailBodyDTO.Items.Item item = placeDetail.getResponse().getBody().getItems().getItem().get(0);
            placeInfoList.add(item);
        }
        return placeInfoList;
    }
}
