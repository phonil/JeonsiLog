package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.infrastructure.openApi.batch.writer.dto.ExhibitionDtoToWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ExhibitionRepositoryCustomImpl implements ExhibitionRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public void bulkUpsertJdbcTemplate(List<ExhibitionDtoToWrite> exhibitionDtoListToWrite) {
        String sql = """
            INSERT INTO exhibition (place_id, name, image_url, operating_keyword, price_keyword, price, start_date, end_date, information, rate, exhibition_seq, sequence, created_date, modified_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), 'ACTIVE')
            ON DUPLICATE KEY UPDATE 
                place_id = VALUES(place_id),
                name = VALUES(name),
                image_url = VALUES(image_url),
                operating_keyword = VALUES(operating_keyword),
                price_keyword = VALUES(price_keyword),
                price = VALUES(price),
                start_date = VALUES(start_date),
                end_date = VALUES(end_date),
                information = VALUES(information),
                rate = VALUES(rate),
                exhibition_seq = VALUES(exhibition_seq),
                sequence = VALUES(sequence),
                modified_date = NOW(),
                status = VALUES(status)
            """;

        jdbcTemplate.batchUpdate(sql,
                exhibitionDtoListToWrite,
                exhibitionDtoListToWrite.size(),
                (ps, dto) -> {
                    ps.setObject(1, dto.getPlaceId(), Types.BIGINT);
                    ps.setString(2, dto.getName());
                    ps.setString(3, dto.getImageUrl());
                    ps.setString(4, dto.getOperatingKeyword().toString());
                    ps.setString(5, dto.getPriceKeyword().toString());
                    ps.setString(6, dto.getPrice());
                    ps.setString(7, dto.getStartDate());
                    ps.setString(8, dto.getEndDate());
                    ps.setString(9, null);
                    ps.setString(10, null);
                    ps.setInt(11, dto.getExhibitionSeq());
                    ps.setInt(12, 11);
                }
        );
    }
}
