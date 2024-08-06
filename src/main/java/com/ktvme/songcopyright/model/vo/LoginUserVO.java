package com.ktvme.songcopyright.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>Description: todo</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: todo</p >
 * <P>Created Date: 2024年08月05日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 上次登录ip地址
     */
    private String lastIp;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 角色编号
     */
    private Integer roleId;
}