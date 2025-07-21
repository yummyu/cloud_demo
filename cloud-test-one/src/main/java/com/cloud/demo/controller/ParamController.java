package com.cloud.demo.controller;

import com.cloud.demo.dto.R;
import com.cloud.demo.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/param")
public class ParamController {

    @PostMapping("check")
    public R param(@RequestBody @Validated UserDto userDto) {
        log.info("okkkkkkkkkkk");
        R.ok(userDto);
        return R.ok();
    }

}
