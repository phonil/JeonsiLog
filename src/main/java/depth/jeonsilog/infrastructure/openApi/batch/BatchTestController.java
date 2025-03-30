package depth.jeonsilog.infrastructure.openApi.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.OpenApiCaller;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class BatchTestController {

    private final BatchStep batchStep;
    private final TaskletBatchStep taskletBatchStep;
    private final OpenApiCaller openApiCaller;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    @GetMapping("/chunk")
    public String batchChunk() throws IOException {
        logger.info("####### [Batch Chunk Step Call] #######");
        batchStep.step();
        logger.info("####### [Batch Chunk Step Returned] #######");
        return "call success !";
    }

    @MethodTimer
    @GetMapping("/tasklet")
    public String batchTasklet() throws IOException {
        logger.info("####### [Batch Tasklet Step Call] #######");
        taskletBatchStep.step();
        logger.info("####### [Batch Tasklet Step Returned] #######");
        return "call success !";
    }
}
