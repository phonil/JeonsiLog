package depth.jeonsilog.global.aop;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiTimerAspect {

    private final MeterRegistry meterRegistry;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(MethodTimer)")
    public Object measureMethodCallTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Timer.Sample sample = Timer.start(meterRegistry);
//        logger.info("## Method Timer ## [" + methodName + "]: {}", );
        Object result = joinPoint.proceed();
//        sample.stop(Timer.builder(className + "." + methodName + ".call")
        sample.stop(Timer.builder("method.call")
                .description(methodName + " 메소드 호출 시간")
                .tag("class", className)
                .tag("method", methodName)
                .tag("status", "success")
                .register(meterRegistry));
        return result;
    }
}
