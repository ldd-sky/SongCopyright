package com.ktvme.songcopyright.service;

import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.par.LoginPar;
import com.ktvme.songcopyright.model.par.UserPar;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 用户业务操作接口</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public interface UserService {
    /**
     * 登陆
     */
    Result login(HttpServletRequest request, LoginPar par);

    /**
     * 发送邮件验证码
     */
    Result sendEmail(UserPar userPar);
}