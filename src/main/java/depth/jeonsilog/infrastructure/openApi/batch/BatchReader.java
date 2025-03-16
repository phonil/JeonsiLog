package depth.jeonsilog.infrastructure.openApi.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.infrastructure.openApi.DataTypeTransferUtil;
import depth.jeonsilog.infrastructure.openApi.OpenApiCaller;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionListDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BatchReader {

    private final OpenApiCaller openApiCaller;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<Integer> readExhibitionList() throws IOException {
        String fromDate = LocalDate.now().minusMonths(3).format(formatter);
        String toDate = LocalDate.now().plusMonths(1).format(formatter);
        List<Integer> performanceSeqList = new ArrayList<>();
        // TODO : 호출 횟수 -> 토큰 버킷 이런거? (page 계산, null break 말고 ..)
        Integer page = 1;
        while (true) {
            // Description : Exhibition List
            String exhibitionListXml = openApiCaller.callExhibitionListApi(fromDate, toDate, page, 100);
            String exhibitionListJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionListXml);
            ExhibitionListDTO exhibitionList = objectMapper.readValue(exhibitionListJsonStr, ExhibitionListDTO.class);
            // 마지막 페이지 지난 경우
            if (exhibitionList.getResponse().getMsgBody().getPerforList() == null)
                break;
            List<ExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListMsgBodyDTO.PerformElement> performanceList = exhibitionList.getResponse().getMsgBody().getPerforList();
            for (ExhibitionListDTO.ExhibitionListResponseDTO.ExhibitionListMsgBodyDTO.PerformElement performElement : performanceList) {
                if (!performanceSeqList.contains(performElement.getSeq()))
                    performanceSeqList.add(performElement.getSeq());
            }
            page++;
        }
        return performanceSeqList;
    }

    public List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> readExhibitionDetail(List<Integer> exhibitionSeqList) throws IOException {
        List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList = new ArrayList<>();
        for (Integer exhibitionSeq : exhibitionSeqList) {
            String exhibitionDetailXml = openApiCaller.callExhibitionDetailApi(exhibitionSeq);
            String exhibitionDetailJsonStr = DataTypeTransferUtil.xmlStrToJsonStr(exhibitionDetailXml);
            ExhibitionDetailDTO exhibitionDetail = objectMapper.readValue(exhibitionDetailJsonStr, ExhibitionDetailDTO.class);
            if (exhibitionDetail.getResponse().getMsgBody().getPerforInfo() == null)
                continue;
            ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo = exhibitionDetail.getResponse().getMsgBody().getPerforInfo();
            performanceInfoList.add(performanceInfo);
        }
        return performanceInfoList;
    }

    public List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> readPlaceDetail(List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList) throws IOException {
        List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = new ArrayList<>();
        for (ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo performanceInfo : performanceInfoList) {
            Integer placeSeq = performanceInfo.getPlaceSeq();
            if (placeSeq == 0)
                continue;
            String placeDetailXml = openApiCaller.callPlaceDetailApi(performanceInfo.getPlaceSeq());
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
