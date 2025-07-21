package com.cloud.demo.advice;

import com.cloud.demo.dto.R;
import com.cloud.demo.enums.ResultCode;
import com.cloud.demo.exception.BusinessException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public R<?> handleBusinessException(BusinessException e) {
        return R.fail(e.getMessage());
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));
        return R.fail(errorMsg);
    }

    /**
     * 适用于 @RequestBody 参数解析
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getRootCause();

        // 1. 处理类型转换错误（如字符串转整数）
        if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) rootCause;

            // 获取字段路径（支持嵌套对象）
            String fieldPath = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            // 获取目标类型
            Class<?> targetType = ife.getTargetType();
            String typeName = targetType != null ? targetType.getSimpleName() : "未知类型";

            // 获取错误值
            Object invalidValue = ife.getValue();
            String valueStr = (invalidValue != null) ? invalidValue.toString() : "null";

            // 构建错误消息
            String errorMsg = String.format("字段 '%s' 类型错误！需要 %s 类型，但收到: '%s'",
                    fieldPath, typeName, valueStr);

            log.warn("JSON解析错误: {}", errorMsg);
            return R.fail(errorMsg);
        }

        // 2. 处理其他JSON解析错误（如JSON格式错误）
        String errorMsg = "请求体JSON格式错误";
        if (rootCause instanceof JsonParseException) {
            errorMsg = "JSON格式错误: " + rootCause.getMessage();
        }

        log.error("请求体解析失败", ex);
        return R.fail(errorMsg);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R<?> handleAllException(Exception e) {
        log.error("System error", e);
        return R.response(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage(), null);
    }

}