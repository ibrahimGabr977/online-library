package com.igb.web.online_library.util;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Before("execution(* com.igb.web.online_library.*.*(..))")
    public void logBeforeInvoke(JoinPoint joinPoint){
        log.info("Before invoked: {}", joinPoint.getSignature());
    }



    @After("execution(* com.igb.web.online_library.*.*(..))")
    public void logAfterInvoke(JoinPoint joinPoint, Object result){
        log.info("After invoked: {}, Result: {}", joinPoint.getSignature(), result);
    }


}
