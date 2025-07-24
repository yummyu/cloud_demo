package com.cloud.demo;

import cn.hutool.core.util.StrUtil;
import com.cloud.demo.dto.UserDto;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest extends TestCase {

    @Test
    public void testApp() {

        List<UserDto> list = new ArrayList<>();
        UserDto userDto;
        for (int i = 0; i < 10; i++) {
            userDto = new UserDto(StrUtil.uuid(), "name" + i, i, "email" + i );
            list.add(userDto);
        }

        LinkedHashMap<String, UserDto> collect = list.stream().collect(Collectors.toMap(UserDto::getId, dto -> dto, (dto1, dto2) -> dto1, LinkedHashMap::new));

        log.info("{}", collect);


    }

    @Test
    public void testDateParse() {

        Date date = new Date();
        String dateStr = date.toString();
        log.info("{}", dateStr);

        // 定义原始日期格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        // 解析原始日期字符串
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);

        // 定义目标日期格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化为目标格式
        String format = dateTime.format(outputFormatter);
        log.info("{}", format);


    }

}
