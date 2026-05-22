package com.example.Olympics.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.example.Olympics.service.*.*(..))")
    public Object logServiceCall(ProceedingJoinPoint pjp) throws Throwable {
        String method = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();

        log.info("→ Calling: {} with args: {}", method, Arrays.toString(args));
        long start = System.currentTimeMillis();

        Object result = pjp.proceed();

        long elapsed = System.currentTimeMillis() - start;
        log.info("← Done: {} in {}ms", method, elapsed);

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.example.Olympics.service.*.*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Exception ex) {
        log.error("✗ Exception in {}: {}", jp.getSignature().toShortString(), ex.getMessage());
    }
}
