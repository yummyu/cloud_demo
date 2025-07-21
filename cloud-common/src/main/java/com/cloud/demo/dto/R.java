package com.cloud.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private Integer code;    // 状态码
    private String msg;     // 消息提示
    private T data;         // 响应数据
    private Long timestamp; // 时间戳

    // 成功响应（无数据）
    public static <T> R<T> ok() {
        return new R<>(200, "操作成功", null, System.currentTimeMillis());
    }

    // 成功响应（带数据）
    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data, System.currentTimeMillis());
    }

    // 失败响应
    public static <T> R<T> fail(String msg) {
        return new R<>(500, msg, null, System.currentTimeMillis());
    }

    // 自定义响应
    public static <T> R<T> response(Integer code, String msg, T data) {
        return new R<>(code, msg, data, System.currentTimeMillis());
    }
}