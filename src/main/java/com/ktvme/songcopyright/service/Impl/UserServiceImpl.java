package com.ktvme.songcopyright.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ktvme.songcopyright.config.JwtConfig;
import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.entity.UserDO;
import com.ktvme.songcopyright.model.par.LoginPar;
import com.ktvme.songcopyright.model.par.UserEmailPar;
import com.ktvme.songcopyright.model.par.UserRegPar;
import com.ktvme.songcopyright.model.vo.LoginUserVO;
import com.ktvme.songcopyright.model.vo.UserEmail;
import com.ktvme.songcopyright.service.UserService;
import com.ktvme.songcopyright.service.dao.UserDOService;
import com.ktvme.songcopyright.utils.IPUtils;
import com.ktvme.songcopyright.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 用户业务接口实现</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDOService userDOService;
    private final RocketMQTemplate rocketMQTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtConfig jwtConfig;

    @Override
    public Result login(HttpServletRequest request, LoginPar par) {
        // 测试数据
        if (par.getUsername().equals("lyh") && par.getPassword().equals("123456")) {
            UserDO user = userDOService.getOne(
                    Wrappers.<UserDO>query().lambda()
                            .eq(UserDO::getUserName, par.getUsername())
            );
            LoginUserVO userVO = LoginUserVO.builder()
                    .userName(user.getUserName())
                    .id(user.getId())
                    .realName(user.getRealName())
                    .roles(user.getRoles())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
            String token = JwtUtil.generateToken(userVO, jwtConfig.getExpire(), jwtConfig.getPrivateKey());
            return Result.success("登陆成功").append("token", token);
        }
        // 校验验证码
        String redisName = "login_verify_code_" + par.getUsername();
        String redisVerifyCode = stringRedisTemplate.opsForValue().get(redisName);
        stringRedisTemplate.delete(redisName);

        if (redisVerifyCode == null) {
            return Result.failed("验证码无效");
        }

        if (!redisVerifyCode.equalsIgnoreCase(par.getVerifycode())) {
            return Result.failed("验证码错误");
        }


        final UserDO user = userDOService.getOne(
                Wrappers.<UserDO>query().lambda()
                        .eq(UserDO::getUserName, par.getUsername())
        );

        if (null == user) {
            return Result.failed("用户名不存在");
        }

        if (!user.getPassword().equals(par.getPassword())) {
            return Result.failed("密码错误");
        }

        boolean success = userDOService.updateById(
                UserDO.builder().id(user.getId())
                        .ip(IPUtils.getIpAddr(request))
                        .loginTime(LocalDateTime.now())
                        .build()
        );

        if (!success) {
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "登录异常");
        }

        LoginUserVO userVO = LoginUserVO.builder()
                .userName(user.getUserName())
                .id(user.getId())
                .realName(user.getRealName())
                .roles(user.getRoles())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        String token = JwtUtil.generateToken(userVO, jwtConfig.getExpire(), jwtConfig.getPrivateKey());
        return Result.success("登陆成功").append("token", token);

    }

    @Override
    public Result sendEmail(UserEmailPar userEmailPar) {
        int num = RandomUtils.nextInt(1000, 10000);

        // 封装发送邮件的数据
        UserEmail userEmail = UserEmail.builder()
                .email(userEmailPar.getEmail())
                .title("用户登录验证码")
                .text(userEmailPar.getUsername() + "您好，本次验证码为：" + num)
                .build();

        // mq存放
        String userEmailStr = JSONObject.toJSONString(userEmail);
        rocketMQTemplate.convertAndSend("email-topic", userEmailStr);

        // redis存放
        String redisName = "login_verify_code_" + userEmailPar.getUsername();
        stringRedisTemplate.opsForValue().set(redisName, num + "");

        return Result.success("验证码发送成功！");
    }

    @Override
    public Result register(HttpServletRequest request, UserRegPar userRegPar) {
        // 校验验证码
        String redisVerifyCode = stringRedisTemplate.opsForValue().get("register" + userRegPar.getUsername());
        stringRedisTemplate.delete("register" + userRegPar.getUsername());
        if (redisVerifyCode == null) {
            return Result.failed("验证码无效");
        }
        if (!redisVerifyCode.equalsIgnoreCase(userRegPar.getVerifycode())) {
            return Result.failed("验证码错误");
        }

        // 密码校验
        if (userRegPar.getPassword() == null) {
            return Result.failed("密码不能为空");
        }
        if (!userRegPar.getPassword().equals(userRegPar.getRepassword())) {
            return Result.failed("密码和确认密码不一致");
        }

        // 注册
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userRegPar.getUsername());
        UserDO findUser = userDOService.getBaseMapper().selectOne(queryWrapper);
        if (findUser != null) {
            return Result.success("用户名已存在");
        }

        UserDO saveUser = UserDO.builder()
                .userName(userRegPar.getUsername())
                .realName(userRegPar.getRealname())
                .phone(userRegPar.getPhone())
                .password(userRegPar.getPassword())
                .email(userRegPar.getEmail())
                .ip(IPUtils.getIpAddr(request))
                .build();
        boolean success = userDOService.save(saveUser);

        if (success) {
            return Result.success("注册成功");
        }

        return Result.success("注册失败");
    }

    @Override
    public Result info(String token) {
        try {
            // 通过token获取user信息
            LoginUserVO user = JwtUtil.getObjectFromToken(token, jwtConfig.getPublicKey(), LoginUserVO.class);

            Map<String, Object> map = new HashMap<>();
            assert user != null;
            if (user.getRoles() != null) {
                map.put("roles", user.getRoles().split(","));        //角色的值必须是数组
            } else {
                map.put("roles", Collections.singletonList("editor"));        //没有权限的固定：editor
            }
            // TODO 完善用户头像
            map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            map.put("name", user.getUserName());
            return Result.success("获得权限成功", map);
        } catch (Exception e) {
            return Result.failed("获得权限失败");
        }
    }
}