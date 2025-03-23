package depth.jeonsilog.infrastructure.openApi.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceUpsertRequest {
    private Integer seq;
    private String name;
    private String address;
    private String tel;
    private String homePage;
}
