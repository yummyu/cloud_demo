package com.cloud.demo.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 统信系统文件名长度限制处理工具类
 * 解决文件名超过255字节的问题，并在截取后添加超长标记
 */
public class FileNameTruncator {

    private static final int MAX_BYTE_LENGTH = 255;
    private static final String ELLIPSIS = "...";
    private static final String TRUNCATION_MARKER = "[超长截取]";
    private static final byte[] ELLIPSIS_BYTES = ELLIPSIS.getBytes(StandardCharsets.UTF_8);
    private static final byte[] MARKER_BYTES = TRUNCATION_MARKER.getBytes(StandardCharsets.UTF_8);

    /**
     * 截取文件名使其不超过255字节，并添加超长标记
     *
     * @param fileName 原始文件名
     * @return 截取后的文件名，包含超长标记
     */
    public static String truncateFileName(String fileName) {
        if (fileName == null) {
            return null;
        }

        // 如果字节长度未超过限制，直接返回
        if (getByteLength(fileName) <= MAX_BYTE_LENGTH) {
            return fileName;
        }

        // 分离文件名和扩展名
        String namePart = "";
        String extension = "";

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            namePart = fileName.substring(0, lastDotIndex);
            extension = fileName.substring(lastDotIndex); // 包含点号
        } else {
            namePart = fileName;
        }

        // 如果扩展名本身就超过了最大长度，直接截取
        if (getByteLength(extension) >= MAX_BYTE_LENGTH) {
            return truncateToByteLength(fileName, MAX_BYTE_LENGTH);
        }

        // 计算可用于文件名部分的最大字节数（包含标记）
        int markerBytes = getByteLength(TRUNCATION_MARKER);
        int availableBytes = MAX_BYTE_LENGTH - markerBytes - getByteLength(extension);

        if (availableBytes <= 0) {
            // 扩展名+标记已占满空间，直接返回截取的扩展名
            return truncateToByteLength(extension, MAX_BYTE_LENGTH);
        }

        // 截取文件名部分
        String truncatedNamePart = truncateToByteLength(namePart, availableBytes);
        return truncatedNamePart + TRUNCATION_MARKER + extension;
    }

    /**
     * 截取完整路径中的文件名部分
     *
     * @param fullPath 完整路径
     * @return 截取后的完整路径
     */
    public static String truncateFilePath(String fullPath) {
        if (fullPath == null) {
            return null;
        }

        Path path = Paths.get(fullPath);
        String fileName = path.getFileName().toString();
        String parentPath = fullPath.substring(0, fullPath.lastIndexOf(File.separator));

        String truncatedFileName = truncateFileName(fileName);

        return parentPath + File.separator + truncatedFileName;
    }

    /**
     * 获取字符串的UTF-8字节长度
     *
     * @param str 输入字符串
     * @return 字节长度
     */
    private static int getByteLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.getBytes(StandardCharsets.UTF_8).length;
    }

    /**
     * 将字符串截取到指定字节长度
     *
     * @param str      原始字符串
     * @param maxBytes 最大字节数
     * @return 截取后的字符串
     */
    private static String truncateToByteLength(String str, int maxBytes) {
        if (str == null || maxBytes <= 0) {
            return "";
        }

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= maxBytes) {
            return str;
        }

        // 从最大字节数开始向前查找，确保不会截断多字节字符
        int actualLength = maxBytes;
        while (actualLength > 0) {
            try {
                String result = new String(bytes, 0, actualLength, StandardCharsets.UTF_8);
                // 验证结果是否符合字节长度要求
                if (result.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
                    return result;
                }
            } catch (Exception e) {
                // 如果出现解码错误，继续减少字节数
            }
            actualLength--;
        }

        return "";
    }
}