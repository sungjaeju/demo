package com.eggseller.test.util;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestAspect {
	private String EXCELUDE_PACKAGE_NAME = "legal.system.controller";

	@Pointcut("execution(public * com.eggseller.test..*Controller.*(..))")
	private void methodLogger() { }

    @Around("publicTarget()")
    public Object calcPerformanceAdvice(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Start measuring performance.");
        StopWatch sw = new StopWatch();
        sw.start();

        Object result = pjp.proceed();

        sw.stop();
        log.info("Stop measuring performance.");
        log.info("Time taken: {} ms", sw.getLastTaskTimeMillis());
        return result;
    }
		
	@Around("execution(* com.eggseller.test..*Controller.*(..))")
	public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			Object result = proceedingJoinPoint.proceed();

			try {
				String classNm = proceedingJoinPoint.getSignature().getDeclaringType().getName();
				String methodName = proceedingJoinPoint.getSignature().getName();

				if(classNm.indexOf(EXCELUDE_PACKAGE_NAME) == -1) {
					//logUtil.writeRequestLog(classNm, methodName);
				}
				log.error("classNm: {}", classNm);
				log.error("methodName: {}", methodName);
				log.error("result: {}", result);
			} catch (Exception e) {
				log.error("LoggerAspect error", e);
			}

			return result;

		} catch (Throwable throwable) {
			throw throwable;
		}
	}
}
