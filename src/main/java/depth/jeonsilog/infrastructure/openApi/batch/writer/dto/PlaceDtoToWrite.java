package depth.jeonsilog.infrastructure.openApi.batch.writer.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class PlaceDtoToWrite {
    private Integer seq;
    private String name;
    private String address;
    private String tel;
    private String homePage;
}
