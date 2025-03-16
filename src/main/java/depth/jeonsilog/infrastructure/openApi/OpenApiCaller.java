package depth.jeonsilog.infrastructure.openApi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallExhibitionDetailConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallExhibitionListConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallPlaceDetailConstant.*;

@RequiredArgsConstructor
@Component
public class OpenApiCaller {

    public String callExhibitionListApi(String from, String to, Integer cPage, Integer rows) throws IOException {
        // 요청 기본 URL
        StringBuilder urlBuilder = new StringBuilder(CALL_EXHIBITION_LIST_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME, ENC) + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(FROM_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(from, ENC));
        urlBuilder.append("&" + URLEncoder.encode(TO_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(to, ENC));
        urlBuilder.append("&" + URLEncoder.encode(C_PAGE_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(cPage.toString(), ENC));
        urlBuilder.append("&" + URLEncoder.encode(ROWS_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(rows.toString(), ENC));

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }

    public String callExhibitionDetailApi(Integer seq) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(CALL_EXHIBITION_DETAIL_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME, ENC) + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(SEQ_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(seq.toString(), ENC));

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }

    // Description : 전시 공간 상세 정보 조회 OPEN API
    public String callPlaceDetailApi(Integer seq) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(CALL_PLACE_DETAIL_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME,ENC) + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(SEQ_PARAMETER_NAME,ENC) + "=" + URLEncoder.encode(seq.toString(), ENC));

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }
}
