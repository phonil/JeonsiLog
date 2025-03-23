package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.infrastructure.openApi.batch.write.PlaceDtoToWrite;
import depth.jeonsilog.infrastructure.openApi.test.PlaceUpsertRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public void bulkUpsertEM(List<PlaceUpsertRequest> requestList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO place (seq, name, address, tel, home_page, created_date, modified_date, status) VALUES ");

        for (int i = 0; i < requestList.size(); i++) {
            PlaceUpsertRequest dto = requestList.get(i);
            if (i > 0) sql.append(",");
            sql.append("(").append(dto.getSeq());
            sql.append(", ").append(quote(dto.getName()));
            sql.append(", ").append(quote(dto.getAddress()));
            sql.append(", ").append(quote(dto.getTel()));
            sql.append(", ").append(quote(dto.getHomePage()));
            sql.append(", NOW(), NOW(), 'ACTIVE'");
            sql.append(")");
        }
        sql.append(" ON DUPLICATE KEY UPDATE ");
        sql.append(" name=VALUES(name),");
        sql.append(" address=VALUES(address),");
        sql.append(" tel=VALUES(tel),");
        sql.append(" home_page=VALUES(home_page),");
        sql.append(" modified_date=NOW(),");
        sql.append(" status=VALUES(status)");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.executeUpdate();
    }

    private String quote(String str) {
        if (str == null) {
            return "NULL";
        }
        return "'" + str.replace("'", "''") + "'";
    }

    public void bulkUpsertEMParameterBinding(List<PlaceUpsertRequest> requestList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO place (seq, name, address, tel, home_page, created_date, modified_date, status) VALUES ");

        for (int i = 0; i < requestList.size(); i++) {
            if (i > 0) sql.append(",");
            sql.append("(?, ?, ?, ?, ?, NOW(), NOW(), 'ACTIVE')");
        }
        sql.append(" ON DUPLICATE KEY UPDATE ");
        sql.append(" name=VALUES(name),");
        sql.append(" address=VALUES(address),");
        sql.append(" tel=VALUES(tel),");
        sql.append(" home_page=VALUES(home_page),");
        sql.append(" modified_date=NOW(),");
        sql.append(" status=VALUES(status)");

        Query query = entityManager.createNativeQuery(sql.toString());

        int paramIndex = 1;
        for (PlaceUpsertRequest dto : requestList) {
            query.setParameter(paramIndex++, dto.getSeq());
            query.setParameter(paramIndex++, dto.getName());
            query.setParameter(paramIndex++, dto.getAddress());
            query.setParameter(paramIndex++, dto.getTel());
            query.setParameter(paramIndex++, dto.getHomePage());
        }
        query.executeUpdate();
    }

    public void bulkUpsertJdbcTemplate(List<PlaceDtoToWrite> placeDtoListToWrite) {
        String sql = """
            INSERT INTO place (seq, name, address, tel, home_page, created_date, modified_date, status)
            VALUES (?, ?, ?, ?, ?, NOW(), NOW(), 'ACTIVE')
            ON DUPLICATE KEY UPDATE 
                name = VALUES(name),
                address = VALUES(address),
                tel = VALUES(tel),
                home_page = VALUES(home_page),
                modified_date = NOW(),
                status = VALUES(status)
            """;

        jdbcTemplate.batchUpdate(sql,
                placeDtoListToWrite,
                placeDtoListToWrite.size(),
                (ps, dto) -> {
                    ps.setLong(1, dto.getSeq());
                    ps.setString(2, dto.getName());
                    ps.setString(3, dto.getAddress());
                    ps.setString(4, dto.getTel());
                    ps.setString(5, dto.getHomePage());
                }
        );
    }
}
