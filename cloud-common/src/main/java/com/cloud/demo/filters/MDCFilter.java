package com.cloud.demo.filters;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MDCFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put("traceId", UUID.fastUUID().toString());
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            log.info("Request URI is: {}", req.getRequestURI());
            chain.doFilter(request, response);  // 调用下一个过滤器或目标资源
        } finally {
            MDC.clear();
        }

    }
}
