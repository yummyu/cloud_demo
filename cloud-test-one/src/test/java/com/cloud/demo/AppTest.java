package com.cloud.demo;

import cn.hutool.core.util.StrUtil;
import com.cloud.demo.dto.UserDto;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

}
