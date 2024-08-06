package com.ktvme.songcopyright.model.par;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

/**
 * <p>Description: 用户注册前端传参</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Valid
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserRegPar {

    private String username;
    private String realname;
    private String password;
    private String email;
    private String phone;
    private String roles;
    private String verifycode;
    private String repassword;
}