package depth.jeonsilog.infrastructure.openApi.batch.reader.dto.afterAPI;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class ChangedExhibitionListDTO {

    @Nullable
    private ExhibitionListResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class ExhibitionListResponseDTO {

        @Nullable
        private ExhibitionListBodyDTO body;

        @Getter
        @NoArgsConstructor
        public static class ExhibitionListBodyDTO {

            private Integer totalCount;
            private Integer PageNo;
            private Integer numOfrows;
            @Nullable
            private Items items;

            @Getter
            @NoArgsConstructor
            public static class Items {
                @Nullable
                private List<Item> item = new ArrayList<>();

                @Getter
                @NoArgsConstructor
                public static class Item {
                    private Integer seq;
                }
            }
        }
    }
}

