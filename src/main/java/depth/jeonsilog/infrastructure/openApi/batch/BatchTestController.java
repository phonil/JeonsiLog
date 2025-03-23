package depth.jeonsilog.infrastructure.openApi.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.DataTypeTransferUtil;
import depth.jeonsilog.infrastructure.openApi.OpenApiCaller;
import depth.jeonsilog.infrastructure.openApi.batch.BatchStep;
import depth.jeonsilog.infrastructure.openApi.dto.API.ExhibitionListDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class BatchTestController {

    private final BatchStep batchStep;
    private final OpenApiCaller openApiCaller;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    @GetMapping
    public String dataBatch() throws IOException {
        logger.info("####### [Batch Step Call] #######");
        batchStep.step();
        logger.info("####### [Batch Step Returned] #######");
        return "call success !";
    }

    @GetMapping("/exhibition-list-test")
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
