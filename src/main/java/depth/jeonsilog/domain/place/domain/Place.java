package depth.jeonsilog.domain.place.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.place.dto.PlaceRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seq", unique = true)
    private Integer seq;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @Column(name = "home_page")
    private String homePage;

    // update 메소드
    public void updatePlaceWithExhibitionDetail(PlaceRequestDto.UpdatePlaceWithExhibitionDetailReq updatePlaceWithExhibitionDetailReq) {
        this.name = updatePlaceWithExhibitionDetailReq.getPlaceName();
        this.address = updatePlaceWithExhibitionDetailReq.getPlaceAddress();
        this.tel = updatePlaceWithExhibitionDetailReq.getPlaceTel();
        this.homePage = updatePlaceWithExhibitionDetailReq.getPlaceHomePage();
    }

    public void updateTel(String tel) {
        this.tel = tel;
    }

    public void updateHomepage(String homePage) {
        this.homePage = homePage;
    }

    @Builder
    public Place(Long id, Integer seq, String name, String address, String tel, String homePage) {
        this.id = id;
        this.seq = seq;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.homePage = homePage;
    }
}
