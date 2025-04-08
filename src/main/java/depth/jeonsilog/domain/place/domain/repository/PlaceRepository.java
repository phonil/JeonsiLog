package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.infrastructure.openApi.batch.reader.dto.PlaceSeqProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

    Optional<Place> findByName(String placeName);

    Optional<Place> findByAddress(String placeAddr);

    Slice<Place> findSliceByNameContainingOrAddressContaining(Pageable pageable, String searchWord, String searchWord2);

    Optional<Place> findBySeq(Integer seq);

    @Modifying
    @Query(value =
            "INSERT INTO place(seq, name, address, tel, home_page, created_date, modified_date, status) " +
                    "VALUES(:seq, :name, :address, :tel, :homePage, NOW(), NOW(), 'ACTIVE') " +
                    "ON DUPLICATE KEY UPDATE " +
                    "name=VALUES(name), " +
                    "address=VALUES(address), " +
                    "tel=VALUES(tel), " +
                    "home_page=VALUES(home_page)",
            nativeQuery = true)
    void upsert(
            @Param("seq") Integer seq,
            @Param("name") String name,
            @Param("address") String address,
            @Param("tel") String tel,
            @Param("homePage") String homePage
    );

    List<PlaceSeqProjection> findBySeqIn(List<Integer> placeSeqList);
}
