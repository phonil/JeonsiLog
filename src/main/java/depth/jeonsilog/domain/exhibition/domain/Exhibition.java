package depth.jeonsilog.domain.exhibition.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.place.domain.Place;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Exhibition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "operating_keyword")
    private OperatingKeyword operatingKeyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_keyword")
    private PriceKeyword priceKeyword;

    @Column(name = "price")
    private String price;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Lob
    @Column(name = "information", columnDefinition = "TEXT")
    private String information;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "exhibition_seq", unique = true, nullable = false)
    private Integer exhibitionSeq;

    @Min(value = 1)
    @Max(value = 11)
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    // update 메소드
    public void updateExhibitionDetail(ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq, String imageUrl) {
        this.name = updateExhibitionDetailReq.getExhibitionName();
        this.operatingKeyword = updateExhibitionDetailReq.getOperatingKeyword();
        this.priceKeyword = updateExhibitionDetailReq.getPriceKeyword();
        this.information = updateExhibitionDetailReq.getInformation();

        if (updateExhibitionDetailReq.getIsImageChange()) { // 이미지 변경하는 경우
            this.imageUrl = imageUrl;
        }
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRate(Double rate) {
        this.rate = rate;
    }

    public void updateSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void updateOperatingKeyword(OperatingKeyword operatingKeyword) {
        this.operatingKeyword = operatingKeyword;
    }

    @Builder
    public Exhibition(Long id, Place place, String name, String imageUrl, OperatingKeyword operatingKeyword, PriceKeyword priceKeyword, String price, String startDate, String endDate, String information, Double rate, Integer exhibitionSeq) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.imageUrl = imageUrl;
        this.operatingKeyword = operatingKeyword;
        this.priceKeyword = priceKeyword;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.information = information;
        this.rate = rate;
        this.sequence = 11; // default = 11
        this.imageUrl = imageUrl;
        this.exhibitionSeq = exhibitionSeq;
    }
}
