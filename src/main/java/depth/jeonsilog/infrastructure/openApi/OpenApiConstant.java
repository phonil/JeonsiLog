package depth.jeonsilog.infrastructure.openApi;

public final class OpenApiConstant {
    private OpenApiConstant() {
    }

    public static final class CallExhibitionListConstant {
        private CallExhibitionListConstant() {
        }
        public static final String CALL_EXHIBITION_LIST_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period";
        public static final String SERVICE_KEY_PARAMETER_NAME = "serviceKey";
        public static final String FROM_PARAMETER_NAME = "from";
        public static final String TO_PARAMETER_NAME = "to";
        public static final String C_PAGE_PARAMETER_NAME = "cPage";
        public static final String ROWS_PARAMETER_NAME = "rows";
    }

    public static final class CallExhibitionDetailConstant {
        private CallExhibitionDetailConstant() {
        }
        public static final String CALL_EXHIBITION_DETAIL_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/d/";
    }

    public static final class CallPlaceDetailConstant {
        private CallPlaceDetailConstant() {
        }
        public static final String CALL_PLACE_DETAIL_URL = "http://www.culture.go.kr/openapi/rest/cultureartspaces/d/";
    }
// =vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D : service 1
    public static final String SERVICE_KEY_1 = "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D";
    public static final String SERVICE_KEY_2 = "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D";
    public static final String ENC = "UTF-8";
    public static final String SEQ_PARAMETER_NAME = "seq";

    public static final class ProcessExhibitionDetailConstant {
        private ProcessExhibitionDetailConstant() {
        }

        public static final String AMP = "%amp;";
        public static final String REPLACE_AMP = "&";

        public static final String LT = "&lt;";
        public static final String REPLACE_LT = "<";

        public static final String GT = "&gt;";
        public static final String REPLACE_GT = ">";

        public static final String QUOT = "&quot;";
        public static final String REPLACE_QUOT = "\"";

        public static final String BACKTICK = "&#39;";
        public static final String REPLACE_BACKTICK = "'";
    }
}
