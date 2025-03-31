package depth.jeonsilog.infrastructure.openApi.test.api_call;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallExhibitionDetailConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallExhibitionListConstant.*;
import static depth.jeonsilog.infrastructure.openApi.OpenApiConstant.CallPlaceDetailConstant.*;

@RequiredArgsConstructor
@Component
public class RestTemplateOpenApiCallerTest {
    private final RestTemplate restTemplate;

    public String callExhibitionListApi(String from, String to, Integer cPage, Integer rows) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(CALL_EXHIBITION_LIST_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME, ENC) + "=" + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(FROM_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(from, ENC));
        urlBuilder.append("&" + URLEncoder.encode(TO_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(to, ENC));
        urlBuilder.append("&" + URLEncoder.encode(C_PAGE_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(cPage.toString(), ENC));
        urlBuilder.append("&" + URLEncoder.encode(ROWS_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(rows.toString(), ENC));

        String url = urlBuilder.toString();
        return restTemplate.getForObject(url, String.class);
    }

    public String callExhibitionDetailApi(Integer seq) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(CALL_EXHIBITION_DETAIL_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME, ENC) + "=" + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(SEQ_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(seq.toString(), ENC));

        String url = urlBuilder.toString();
        return restTemplate.getForObject(url, String.class);
    }

    public String callPlaceDetailApi(Integer seq) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(CALL_PLACE_DETAIL_URL);
        urlBuilder.append("?" + URLEncoder.encode(SERVICE_KEY_PARAMETER_NAME, ENC) + "=" + SERVICE_KEY_1);
        urlBuilder.append("&" + URLEncoder.encode(SEQ_PARAMETER_NAME, ENC) + "=" + URLEncoder.encode(seq.toString(), ENC));

        String url = urlBuilder.toString();
        return restTemplate.getForObject(url, String.class);
    }
}
