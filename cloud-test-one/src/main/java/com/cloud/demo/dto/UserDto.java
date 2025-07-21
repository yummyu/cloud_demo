package com.cloud.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    @NotEmpty(message = "id不能为空")
    private String id;
    @NotEmpty(message = "用户名不能为空")
    private String name;
    @NotNull(message = "年龄不能为空")
    private Integer age;
    @Email(message = "邮箱格式不正确")
    private String email;

}
