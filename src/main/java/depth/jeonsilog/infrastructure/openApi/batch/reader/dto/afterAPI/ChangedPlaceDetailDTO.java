package depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class ChangedPlaceDetailDTO {

    @Nullable
    private PlaceDetailResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class PlaceDetailResponseDTO {

        @Nullable
        private PlaceDetailBodyDTO body;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class PlaceDetailBodyDTO {

            @Nullable
            private Items items;

            @Getter
            @NoArgsConstructor
            public static class Items {

                @Nullable
                private List<Item> item = new ArrayList<>();

                @Getter
                @Setter
                @NoArgsConstructor
                public static class Item {

                    private Integer seq;
                    private String culName;
                    private String culAddr;
                    private String culTel;
                    private String culHomeUrl;
                }
            }
        }
    }
}
