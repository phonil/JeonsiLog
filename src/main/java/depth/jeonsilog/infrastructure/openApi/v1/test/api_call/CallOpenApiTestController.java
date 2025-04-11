package depth.jeonsilog.infrastructure.openApi.v1.test.api_call;

import depth.jeonsilog.infrastructure.openApi.ChangedOpenApiCaller;
import depth.jeonsilog.infrastructure.openApi.OpenApiCaller;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/call-openapi")
public class CallOpenApiTestController {

    private final OpenApiCaller openApiCaller;
    private final ChangedOpenApiCaller changedOpenApiCaller;
    private final RestTemplateOpenApiCallerTest restTemplateOpenApiCallerTest;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/list-restTemplate")
    public void testCallExhibitionListWithRestTemplate() throws UnsupportedEncodingException {
        int totalPages = 6;
        int rows = 1;
        String fromDate = "20250311";
        String toDate = "20250311";
        logger.info("[RestTemplate] 요청 시작");
        String line = restTemplateOpenApiCallerTest.callExhibitionListApi(fromDate, toDate, totalPages, rows);
        logger.info("[RestTemplate] 결과 : {}", line);
        logger.info("[RestTemplate] 요청 종료");
    }

    @GetMapping("/list-httpUrlConnection")
    public void testCallExhibitionListWithhttpUrlConnection() throws IOException {
        int totalPages = 6;
        int rows = 1;
        String fromDate = "20250311";
        String toDate = "20250311";
        logger.info("[httpUrlConnection] 요청 시작");
        String line = changedOpenApiCaller.callExhibitionListApi(fromDate, toDate, totalPages, rows);
        changedOpenApiCaller.callExhibitionDetailApi(285433);
        changedOpenApiCaller.callPlaceDetailApi(2838);
//        String line = openApiCaller.callExhibitionListApi(fromDate, toDate, totalPages, rows);
//        logger.info("[httpUrlConnection] 결과 : {}", line);
        logger.info("[httpUrlConnection] 요청 종료");
    }

    // 비동기 테스트
    @GetMapping("/exhibition-list")
    public void testExhibitionListAsyncWithResult() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3); // 스레드 3개 고정

        int totalPages = 6;
        int rows = 6;
        String fromDate = "20250311";
        String toDate = "20250311";

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 1; page <= totalPages; page++) {
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    logger.info("[Thread: {}] 요청 시작 - step {}", Thread.currentThread().getName(), currentPage);
                    return openApiCaller.callExhibitionListApi(fromDate, toDate, currentPage, rows);
                } catch (Exception e) {
                    logger.error("[Thread: {}] API 호출 실패 - step {}", Thread.currentThread().getName(), currentPage, e);
                    return null;
                }
            }, executor).thenAccept(xml -> {
                if (xml != null) {
                    logger.info("[Thread: {}] 응답 수신 완료 - step {}\n{}", Thread.currentThread().getName(), currentPage, xml);
                }
            });
            futures.add(future);
        }

        // 모든 비동기 작업 완료 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
        logger.info("테스트 완료: exhibition-list API 병렬 호출 + 결과 출력");
    }
}
