package depth.jeonsilog.infrastructure.openApi.batch;

import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.infrastructure.openApi.dto.API.PlaceDetailDTO;
import depth.jeonsilog.infrastructure.openApi.batch.write.ExhibitionDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.batch.write.PlaceDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BatchStep {

    private final BatchReader batchReader;
    private final BatchProcessor batchProcessor;
    private final BatchWriter batchWriter;

    public void step() throws IOException {
        List<Integer> performanceSeqList = batchReader.readExhibitionList();
        List<ExhibitionDetailDTO.ExhibitionDetailResponseDTO.ExhibitionDetailMsgBodyDTO.PerformanceInfo> performanceInfoList = batchReader.readExhibitionDetail(performanceSeqList);
        List<PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo> placeInfoList = batchReader.readPlaceDetail(performanceInfoList);

        List<PlaceDtoToWrite> placeDtoListToWrite = batchProcessor.processPlace(placeInfoList);
        List<ExhibitionDtoToWrite> exhibitionDtoListToWrite = batchProcessor.processExhibition(performanceInfoList);

        batchWriter.writePlace(placeDtoListToWrite);
        batchWriter.writeExhibition(exhibitionDtoListToWrite);
    }
}
