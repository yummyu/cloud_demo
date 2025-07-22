package com.cloud.demo.utils;

import cn.hutool.core.io.FileUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件上传下载工具类
 */
public class FileUpDownUtil {

    /**
     * 下载文件
     *
     * @param response HttpServletResponse对象
     * @param filePath 文件绝对路径
     * @param fileName 下载时显示的文件名
     */
    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) throws IOException {
        File file = FileUtil.newFile(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }

        // 设置响应头
        setResponseHeader(response, fileName);
        response.setContentLengthLong(file.length());
        // 写入文件流
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
    }

    /**
     * 下载字节数组内容为文件
     *
     * @param response HttpServletResponse对象
     * @param bytes    文件内容字节数组
     * @param fileName 下载时显示的文件名
     */
    public static void downloadBytes(HttpServletResponse response, byte[] bytes, String fileName) throws IOException {
        setResponseHeader(response, fileName);
        response.setContentLength(bytes.length);

        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }


    /**
     * 设置响应头
     *
     * @param response HttpServletResponse对象
     * @param fileName 文件名
     */
    private static void setResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        // 设置文件类型
        response.setContentType("application/octet-stream");

        // 设置文件名编码，解决中文乱码问题
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName);

        // 禁用缓存
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

}
