package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;

import java.util.List;

public interface ExhibitionRepositoryCustom {

    void bulkUpsertJdbcTemplate(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite);
}
