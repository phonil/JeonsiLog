package depth.jeonsilog.infrastructure.openApi.batch.write;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ExhibitionDtoToWrite {
    private Integer placeSeq;
    private String name;
    private String imageUrl;
    private OperatingKeyword operatingKeyword;
    private PriceKeyword priceKeyword;
    private String price;
    private String startDate;
    private String endDate;
    private Integer exhibitionSeq;
    private Integer sequence;
}
