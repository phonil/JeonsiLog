package depth.jeonsilog.infrastructure.openApi.batch.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.DataTypeTransferUtil;
import depth.jeonsilog.infrastructure.openApi.OpenApiCaller;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionListDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BatchReader {

    private final OpenApiCaller openApiCaller;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    public List<Integer> readExhibitionList() throws IOException {
        String fromDate = "20250305";
        String toDate = "20250314";
//        String fromDate = LocalDate.now().minusMonths(3).format(formatter);
//        String toDate = LocalDate.now().plusMonths(1).format(formatter);
        List<Integer> performanceSeqList = new ArrayList<>();
        // TODO : 호출 횟수 -> 토큰 버킷 이런거? (page 계산, null break 말고 ..)
        Integer page = 1;
        int rows = 100;
        while (true) {
            logger.info("## Reader ## [Exhibition List Page], {}", page);
            String exhibitionListXml = openApiCaller.callExhibitionListApi(fromDate, toDate, page, rows);
            logger.info("## Reader ## [Exhibition List Function Call Return ( XML String )], {}", exhibitionListXml);
            String exhibitionListJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionListXml);
            ExhibitionListDTO exhibitionList = objectMapper.readValue(exhibitionListJsonStr, ExhibitionListDTO.class);
            // 마지막 페이지 지난 경우
            if (exhibitionList.getResponse().getMsgBody().getPerforList().isEmpty())
                break;
            List<ExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListMsgBodyDTO.PerformElement> performanceList = exhibitionList.getResponse().getMsgBody().getPerforList();
            for (ExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListMsgBodyDTO.PerformElement performElement : performanceList) {
                if (!performanceSeqList.contains(performElement.getSeq()))
                    performanceSeqList.add(performElement.getSeq());
            }
//            Integer totalCount = exhibitionList.getResponse().getMsgBody().getTotalCount();
//            int numOfPages = (totalCount / rows) + 1;
//            if (totalCount % rows == 0)
//                numOfPages = (totalCount) / rows;
//            if (page == numOfPages)
//                break;
            page++;
        }
        return performanceSeqList;
    }

    @MethodTimer
    public List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> readExhibitionDetail(List<Integer> exhibitionSeqList) throws IOException {
        List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList = new ArrayList<>();
        int i = 1;
        for (Integer exhibitionSeq : exhibitionSeqList) {
            String exhibitionDetailXml = openApiCaller.callExhibitionDetailApi(exhibitionSeq);
            logger.info("## Reader ## [Exhibition Detail Function Call Return ( XML String )], {}", exhibitionDetailXml + i++);
            String exhibitionDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionDetailXml);
            ExhibitionDetailDTO exhibitionDetail = objectMapper.readValue(exhibitionDetailJsonStr, ExhibitionDetailDTO.class);
            if (exhibitionDetail.getResponse().getMsgBody().getPerforInfo() == null)
                continue;
            ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo = exhibitionDetail.getResponse().getMsgBody().getPerforInfo();
            performanceInfoList.add(performanceInfo);
        }
        return performanceInfoList;
    }

    @MethodTimer
    public List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> readPlaceDetail(List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList) throws IOException {
        List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = new ArrayList<>();
        int i = 1;
        for (ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo : performanceInfoList) {
            Integer placeSeq = performanceInfo.getPlaceSeq();
            if (placeSeq == 0)
                continue;
            String placeDetailXml = openApiCaller.callPlaceDetailApi(performanceInfo.getPlaceSeq());
            logger.info("## Reader ## [Place Detail Function Call Return ( XML String )], {}", placeDetailXml + i++);
            String placeDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(placeDetailXml);
            PlaceDetailDTO placeDetail = objectMapper.readValue(placeDetailJsonStr, PlaceDetailDTO.class);
            if (placeDetail.getResponse().getMsgBody().getPlaceInfo() == null)
                continue;
            PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo placeInfo = placeDetail.getResponse().getMsgBody().getPlaceInfo();
            placeInfoList.add(placeInfo);
        }
        return placeInfoList;
    }
}
