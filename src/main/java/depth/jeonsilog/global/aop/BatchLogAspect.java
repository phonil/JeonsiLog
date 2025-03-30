package depth.jeonsilog.global.aop;

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
public class BatchLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(BatchLog)")
    public Object measureMethodCallTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("## Batch Log ## [ " + methodName + " ] of [ " + className + " ] Call");
        Object result = joinPoint.proceed();
        logger.info("## Batch Log ## [ " + methodName + " ] of [ " + className + " ] Returned");
        return result;
    }
}

