package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.processor.BatchProcessor;
import depth.jeonsilog.infrastructure.openApi.batch.reader.BatchReader;
import depth.jeonsilog.infrastructure.openApi.batch.reader.TaskletBatchReader;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI.ChangedPlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.writer.TaskletBatchWriter;
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
public class TaskletBatchStep {

    private final TaskletBatchReader taskletBatchReader;
    private final BatchReader batchReader;
    private final BatchProcessor batchProcessor;
    private final TaskletBatchWriter taskletBatchWriter;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    public void step() throws IOException {
        List<Integer> performanceSeqList = batchReader.readExhibitionList();
        List<ChangedExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailBodyDTO.Items.Item> performInfoList = batchReader.readExhibitionDetail(performanceSeqList);
        List<ChangedPlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailBodyDTO.Items.Item> placeInfoList = batchReader.readPlaceDetail(performInfoList);

        List<PlaceDtoToWrite> placeDtoListToWrite = batchProcessor.processPlace(placeInfoList);
        List<ExhibitionDtoToWrite> exhibitionDtoListToWrite = batchProcessor.processExhibition(performInfoList);

        taskletBatchWriter.writePlace(placeDtoListToWrite);
        taskletBatchWriter.writeExhibition(exhibitionDtoListToWrite);
//        List<Integer> seqList = taskletBatchWriter.writePlaceBulk(placeDtoListToWrite);
//        taskletBatchWriter.writeExhibitionBulk(exhibitionDtoListToWrite, seqList);
    }
}
