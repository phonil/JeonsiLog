package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.infrastructure.openApi.batch.write.ExhibitionDtoToWrite;

import java.util.List;

public interface ExhibitionRepositoryCustom {

    void bulkUpsertJdbcTemplate(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite);
}
