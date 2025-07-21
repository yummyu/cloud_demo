package com.cloud.demo.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求响应日志切面
 */
@Slf4j
@Aspect
@Component
public class RRLogAspect {

    /**
     * execution(modifiers-pattern? return-type-pattern declaring-type-pattern? method-name-pattern(param-pattern) throws-pattern?)
     * modifiers-pattern：方法的修饰符，如 public、private 等（可选）。
     * return-type-pattern：方法返回类型，如 void、String、*（任意返回类型）。
     * declaring-type-pattern：声明方法的类或接口的全限定名（可选）。
     * method-name-pattern：方法名，支持通配符。
     * param-pattern：方法参数列表，使用 () 表示无参，(String, int) 表示具体参数，(..) 表示任意参数。
     * throws-pattern：方法抛出的异常类型（可选）。
     *
     * =========================|================
     * execution1 || execution2 | 匹配任意一个表达式
     * -------------------------|----------------
     * execution1 && execution2 | 同时满足两个表达式
     *
     */
    @Pointcut("execution(* com.cloud.demo.controller.*.*(..))")
    public void logPointcut() {
    }


    /**
     * https://www.cnblogs.com/manmanblogs/p/15871284.html
     * @param joinPoint
     * @throws Throwable
     */
    @Around("logPointcut()")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志    
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
//        log.info("Request Args   : {}", new Gson().toJson(joinPoint.getArgs()));
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Request Args   : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(joinPoint.getArgs()));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        // 打印出参
        log.info("Response Args  : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("=========================================== End ===========================================");
        // 每个请求之间空一行
        log.info("");
    }

}
