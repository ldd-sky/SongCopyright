package com.ktvme.songcopyright.controller;

import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.par.LoginPar;
import com.ktvme.songcopyright.model.par.UserPar;
import com.ktvme.songcopyright.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result sendEmail(UserPar userPar) {
        return userService.sendEmail(userPar);
    }

    @GetMapping("/info")
    public Result info(String token) {
        Map<String, Object> map = new HashMap<>();
        if ("admin-token".equals(token)){
            map.put("roles", Collections.singletonList("admin"));
            map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            map.put("name", "Super Admin");
            map.put("introduction", "None");
        } else{
            map.put("roles", Collections.singletonList("editor"));
            map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            map.put("name", "Normal Editor");
            map.put("introduction", "None");
        }
        return Result.success(map);
    }
}