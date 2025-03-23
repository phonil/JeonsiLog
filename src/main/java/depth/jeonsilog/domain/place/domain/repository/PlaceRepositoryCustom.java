package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.PlaceDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.test.PlaceUpsertRequest;

import java.util.List;

public interface PlaceRepositoryCustom {

    void bulkUpsertEM(List<PlaceUpsertRequest> requestList);
    void bulkUpsertEMParameterBinding(List<PlaceUpsertRequest> requestList);
    void bulkUpsertJdbcTemplate(List<PlaceDtoToWrite> placeDtoListToWrite);
}
