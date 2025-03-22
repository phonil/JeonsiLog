package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

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
                    "home_page=VALUES(home_page), " +
                    "modified_date=NOW()",
            nativeQuery = true)
    void upsertPlace(
            @Param("seq") Long seq,
            @Param("name") String name,
            @Param("address") String address,
            @Param("tel") String tel,
            @Param("homePage") String homePage
    );
}
