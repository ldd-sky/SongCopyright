package com.ktvme.songcopyright.controller;

import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.par.LoginPar;
import com.ktvme.songcopyright.model.par.UserEmailPar;
import com.ktvme.songcopyright.model.par.UserRegPar;
import com.ktvme.songcopyright.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.lwawt.macosx.CSystemTray;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 登陆控制器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月05日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    /**
     * 用户名登陆
     */
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @Valid LoginPar par) {
        return userService.login(request, par);
    }

    @PostMapping("/send-email")
    public Result sendEmail(UserEmailPar userEmailPar) {
        return userService.sendEmail(userEmailPar);
    }

    @GetMapping("/info")
    public Result info(String token) {
        return userService.info(token);
    }

    @PostMapping("/register")
    public Result register(HttpServletRequest request, UserRegPar userRegPar){
        return userService.register(request, userRegPar);
    }

    @PostMapping("/logout/{token}")
    public Result logout(@PathVariable("token") String token){
        return Result.success("退出登录");
    }
}