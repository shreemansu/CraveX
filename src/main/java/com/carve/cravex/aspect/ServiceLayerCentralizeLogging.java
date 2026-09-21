package com.carve.cravex.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Component
@Aspect
@Slf4j
public class ServiceLayerCentralizeLogging {

    @Around("execution(* com.carve.cravex.serviceimpl..*(..))")
    public Object loggingAndTimeChecking(ProceedingJoinPoint joinPoint) throws Throwable {
        long start=System.currentTimeMillis();
        try {
           Object res=joinPoint.proceed();
           log.info("Method Executed "+joinPoint.getSignature().getDeclaringTypeName()+" "+joinPoint.getSignature().getName());
           log.warn("Execution Time "+joinPoint.getSignature().getDeclaringTypeName()+" "+joinPoint.getSignature().getName()+" "+(System.currentTimeMillis()-start+"ms"));
           return res;
        }catch (Throwable t){
            log.error("Error Occured "+joinPoint.getSignature().getDeclaringTypeName()+" "+joinPoint.getSignature().getName()+" "+t.getMessage());
            log.warn("Execution Time "+joinPoint.getSignature().getDeclaringTypeName()+" "+joinPoint.getSignature().getName()+" "+(System.currentTimeMillis()-start));
            throw t;
        }
    }
}
