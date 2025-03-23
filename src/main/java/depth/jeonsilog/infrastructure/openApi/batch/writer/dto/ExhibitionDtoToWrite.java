package depth.jeonsilog.infrastructure.openApi.batch.writer.dto;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ExhibitionDtoToWrite {
    private Long placeId;
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
