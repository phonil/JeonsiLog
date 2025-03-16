package depth.jeonsilog.infrastructure.openApi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Component
public class OpenApiReader {

    // Description : 전시회 목록 조회 OPEN API  /  파라미터로 cPage / from, to
    public String callExhibitionListApi(String from, String to, Integer cPage, Integer rows) throws IOException {
        // 요청 기본 URL
        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period");
        // 파라미터 추가 : 서비스 키, 날짜 범위 (from, to), 현재 페이지, 페이지 당 가져올 개수
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("from","UTF-8") + "=" + URLEncoder.encode(from, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("to","UTF-8") + "=" + URLEncoder.encode(to, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("cPage","UTF-8") + "=" + URLEncoder.encode(cPage.toString(), "UTF-8")); // 요청 페이지
        urlBuilder.append("&" + URLEncoder.encode("rows","UTF-8") + "=" + URLEncoder.encode(rows.toString(), "UTF-8")); // 페이지 당 가져올 개수

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }

    // Description : 전시회 상세 정보 조회 OPEN API  /  파라미터로 seq
    public String callExhibitionDetailApi(Integer seq) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/d/");
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("seq","UTF-8") + "=" + URLEncoder.encode(seq.toString(), "UTF-8")); // 전시회 번호 261752, 246264 ...

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }

    // Description : 전시 공간 상세 정보 조회 OPEN API
    public String callPlaceDetailApi(Integer seq) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/cultureartspaces/d/");
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("seq","UTF-8") + "=" + URLEncoder.encode(seq.toString(), "UTF-8")); // 전시공간 seq

        HttpURLConnection connection = OpenApiUtil.createHttpURLConnectionGet(urlBuilder);
        StringBuilder stringBuilder = OpenApiUtil.writeResponse(connection);
        connection.disconnect();
        return stringBuilder.toString();
    }
}
