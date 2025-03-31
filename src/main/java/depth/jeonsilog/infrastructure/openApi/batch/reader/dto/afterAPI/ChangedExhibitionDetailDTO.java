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
public class ChangedExhibitionDetailDTO {

    @Nullable
    private ExhibitionDetailResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class ExhibitionDetailResponseDTO {

        @Nullable
        private ExhibitionDetailBodyDTO body;

        @Getter
        @NoArgsConstructor
        public static class ExhibitionDetailBodyDTO {

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
                    private String title;
                    private String startDate;
                    private String endDate;
                    private String price;
                    private Integer placeSeq;
                    private String imgUrl;
                }
            }
        }
    }
}

