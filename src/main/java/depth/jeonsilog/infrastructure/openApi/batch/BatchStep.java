package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.write.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.write.PlaceDtoToWrite;
import io.micrometer.core.annotation.Timed;
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

    @MethodTimer
    public void step() throws IOException {
        logger.info("####### [Batch Reader Exhibition List Call] #######");
        List<Integer> performanceSeqList = batchReader.readExhibitionList();
        logger.info("####### [Batch Reader Exhibition List Returned] #######");

        logger.info("####### [Batch Reader Exhibition Detail Call] #######");
        List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList = batchReader.readExhibitionDetail(performanceSeqList);
        logger.info("####### [Batch Reader Exhibition Detail Returned] #######");

        logger.info("####### [Batch Reader Place Detail Call] #######");
        List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = batchReader.readPlaceDetail(performanceInfoList);
        logger.info("####### [Batch Reader Place Detail Returned] #######");

        logger.info("####### [Batch Processor PlaceToWrite Call] #######");
        List<PlaceDtoToWrite> placeDtoListToWrite = batchProcessor.processPlace(placeInfoList);
        logger.info("####### [Batch Processor PlaceToWrite Returned] #######");

        logger.info("####### [Batch Processor ExhibitionToWrite Call] #######");
        List<ExhibitionDtoToWrite> exhibitionDtoListToWrite = batchProcessor.processExhibition(performanceInfoList);
        logger.info("####### [Batch Processor ExhibitionToWrite Returned] #######");

        logger.info("####### [Batch Writer Place Call] #######");
        batchWriter.writePlace(placeDtoListToWrite);
        logger.info("####### [Batch Writer Place Returned] #######");

        logger.info("####### [Batch Writer Exhibition Call] #######");
        batchWriter.writeExhibition(exhibitionDtoListToWrite);
        logger.info("####### [Batch Writer Exhibition Returned] #######");
    }
}
