package com.cloud.demo.controller;

import cn.hutool.core.io.FileUtil;
import com.cloud.demo.dto.R;
import com.cloud.demo.dto.UserDto;
import com.cloud.demo.utils.FileUpDownUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/param")
public class ParamController {

    @PostMapping("paramCheck")
    public R paramCheck(@RequestBody @Validated UserDto userDto) {
        log.info("okkkkkkkkkkk");
        return R.ok(userDto);
    }

    @PostMapping("uploadFile")
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename: {}", originalFilename);
        log.info("type: {}", type);
        return R.ok(file.getOriginalFilename());
    }

    @GetMapping("download/{id}")
    public void download(@PathVariable("id") String id, HttpServletResponse httpResponse) throws IOException {
        log.info("id: {}", id);
        FileUpDownUtil.downloadFile(httpResponse, "文件路径", "文件名.文件类型");
    }

}
