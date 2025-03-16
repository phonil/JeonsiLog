package depth.jeonsilog.infrastructure.openApi.batch.write;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class PlaceDtoToWrite {
    private Integer seq;
    private String name;
    private String address;
    private String tel;
    private String homePage;
}
