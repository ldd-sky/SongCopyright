package com.ktvme.songcopyright.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>Description: 表user</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
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
@TableName(value = "user")
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 真实名称
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * ip地址
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    /**
     * 角色编号
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * 数据创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 数据更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}