package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.global.aop.BatchLog;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.processor.BatchProcessor;
import depth.jeonsilog.infrastructure.openApi.batch.reader.BatchReader;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedPlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.writer.BatchWriter;
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
    public void step() throws IOException, InterruptedException {
        List<Integer> performanceSeqList = batchReader.readExhibitionList();
        int chunkSize = 10;
        int total = performanceSeqList.size();
        int startIdx = 0;

        while (startIdx < total) {
            int endIdx = Math.min(startIdx + chunkSize, total);
            List<Integer> chunkSeqList = performanceSeqList.subList(startIdx, endIdx);
            List<ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item> performInfoList = batchReader.readExhibitionDetail(chunkSeqList);
            List<ChangedPlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailBodyDTO.Items.Item> placeInfoList = batchReader.readPlaceDetail(performInfoList);

            List<PlaceDtoToWrite> placeDtoList = batchProcessor.processPlace(placeInfoList);
            List<ExhibitionDtoToWrite> exhibitDtoList = batchProcessor.processExhibition(performInfoList);

            List<Integer> placeSeqList = batchWriter.writePlace(placeDtoList);
            batchWriter.writeExhibition(exhibitDtoList, placeSeqList);

            startIdx = endIdx;
//            Thread.sleep(500);
        }
    }
}
