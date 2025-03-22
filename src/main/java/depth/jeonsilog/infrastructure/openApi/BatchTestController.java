package depth.jeonsilog.infrastructure.openApi;

import depth.jeonsilog.global.aop.MethodTimer;
import depth.jeonsilog.infrastructure.openApi.batch.BatchStep;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @MethodTimer
    @GetMapping
    public String dataBatch() throws IOException {
        logger.info("####### [Batch Step Call] #######");
        batchStep.step();
        logger.info("####### [Batch Step Returned] #######");
        return "call success !";
    }
}
