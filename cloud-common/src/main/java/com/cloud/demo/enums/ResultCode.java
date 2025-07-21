package com.cloud.demo.enums;

import lombok.Getter;

@Getter
public enum ResultCode  {
    SUCCESS(200, "操作成功"),
    FAILURE(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统异常");

    private final int code;
    private final String message;

    ResultCode (int code, String message) {
        this.code = code;
        this.message = message;
    }


}