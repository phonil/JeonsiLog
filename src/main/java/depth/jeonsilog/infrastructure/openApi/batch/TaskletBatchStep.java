package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.processor.BatchProcessor;
import depth.jeonsilog.infrastructure.openApi.batch.reader.TaskletBatchReader;
import depth.jeonsilog.infrastructure.openApi.batch.writer.TaskletBatchWriter;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.beforeAPI.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.beforeAPI.PlaceDetailDTO;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskletBatchStep {

    private final TaskletBatchReader taskletBatchReader;
    private final BatchProcessor batchProcessor;
    private final TaskletBatchWriter taskletBatchWriter;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    @Scheduled(cron = "0 0 3 * * *")
    @SchedulerLock(
            name = "data_upsert_batch",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT5M"
    )
    public void step() throws IOException {
        logger.info("####### [Batch Reader Exhibition List Call] #######");
        List<Integer> performanceSeqList = taskletBatchReader.readExhibitionList();
        logger.info("####### [Batch Reader Exhibition List Returned] #######");

        logger.info("####### [Batch Reader Exhibition Detail Call] #######");
        List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList = taskletBatchReader.readExhibitionDetail(performanceSeqList);
        logger.info("####### [Batch Reader Exhibition Detail Returned] #######");

        logger.info("####### [Batch Reader Place Detail Call] #######");
        List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = taskletBatchReader.readPlaceDetail(performanceInfoList);
        logger.info("####### [Batch Reader Place Detail Returned] #######");

        logger.info("####### [Batch Processor PlaceToWrite Call] #######");
        List<PlaceDtoToWrite> placeDtoListToWrite = batchProcessor.processPlace(placeInfoList);
        logger.info("####### [Batch Processor PlaceToWrite Returned] #######");

        logger.info("####### [Batch Processor ExhibitionToWrite Call] #######");
        List<ExhibitionDtoToWrite> exhibitionDtoListToWrite = batchProcessor.processExhibition(performanceInfoList);
        logger.info("####### [Batch Processor ExhibitionToWrite Returned] #######");

        logger.info("####### [Batch Writer Place Call] #######");
        taskletBatchWriter.writePlace(placeDtoListToWrite);
//        List<Integer> seqList = taskletBatchWriter.writePlaceBulk(placeDtoListToWrite);
        logger.info("####### [Batch Writer Place Returned] #######");

        logger.info("####### [Batch Writer Exhibition Call] #######");
        taskletBatchWriter.writeExhibition(exhibitionDtoListToWrite);
//        taskletBatchWriter.writeExhibitionBulk(exhibitionDtoListToWrite, seqList);
        logger.info("####### [Batch Writer Exhibition Returned] #######");
    }
}
