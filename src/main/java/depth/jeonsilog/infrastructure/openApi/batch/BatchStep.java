package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.global.aop.BatchLog;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.processor.BatchProcessor;
import depth.jeonsilog.infrastructure.openApi.batch.reader.BatchReader;
import depth.jeonsilog.infrastructure.openApi.batch.writer.BatchWriter;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.beforeAPI.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.beforeAPI.PlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BatchStep {

    private final BatchReader batchReader;
    private final BatchProcessor batchProcessor;
    private final BatchWriter batchWriter;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @BatchLog
    @MethodTimer
    public void step() throws IOException {
        List<Integer> performanceSeqList = batchReader.readExhibitionList();

        int chunkSize = 100;
        int total = performanceSeqList.size();
        int startIdx = 0;

        while (startIdx < total) {
            int endIdx = Math.min(startIdx + chunkSize, total);
            // 1) 이번 chunk의 seq
            List<Integer> chunkSeqList = performanceSeqList.subList(startIdx, endIdx);

            // 2) Read detail for chunk
            List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> perfInfoList = batchReader.readExhibitionDetail(chunkSeqList);
            List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = batchReader.readPlaceDetail(perfInfoList);

            // 3) Process
            List<PlaceDtoToWrite> placeDtoList = batchProcessor.processPlace(placeInfoList);
            List<ExhibitionDtoToWrite> exhibitDtoList = batchProcessor.processExhibition(perfInfoList);

            // 4) Write
            List<Integer> placeSeqList = batchWriter.writePlace(placeDtoList);
            batchWriter.writeExhibition(exhibitDtoList, placeSeqList);

            // 5) move next chunk
            startIdx = endIdx;
        }
    }
}
