package com.ktvme.songcopyright.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.entity.UserDO;
import com.ktvme.songcopyright.model.par.LoginPar;
import com.ktvme.songcopyright.model.par.UserPar;
import com.ktvme.songcopyright.model.vo.UserEmail;
import com.ktvme.songcopyright.service.UserService;
import com.ktvme.songcopyright.service.dao.UserDOService;
import com.ktvme.songcopyright.utils.IPUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(HttpServletRequest request, LoginPar par) {
        // 测试数据
        if (par.getUsername().equals("lyh") && par.getPassword().equals("123456")) {
            return Result.success("登陆成功").append("token", "admin-token");
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

        String token = "admin-token";
        return Result.success("登陆成功").append("token", token);

    }

    @Override
    public Result sendEmail(UserPar userPar) {
        int num = RandomUtils.nextInt(1000, 10000);

        // 封装发送邮件的数据
        UserEmail userEmail = UserEmail.builder()
                .email(userPar.getEmail())
                .title("用户登录验证码")
                .text(userPar.getUsername() + "您好，本次验证码为：" + num)
                .build();

        // mq存放
        String userEmailStr = JSONObject.toJSONString(userEmail);
        rocketMQTemplate.convertAndSend("email-topic", userEmailStr);

        // redis存放
        String redisName = "login_verify_code_" + userPar.getUsername();
        stringRedisTemplate.opsForValue().set(redisName, num + "");

        return Result.success("验证码发送成功！");
    }
}