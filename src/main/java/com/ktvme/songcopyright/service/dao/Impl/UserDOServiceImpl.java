package com.ktvme.songcopyright.service.dao.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktvme.songcopyright.dao.UserDao;
import com.ktvme.songcopyright.model.entity.UserDO;
import com.ktvme.songcopyright.service.dao.UserDOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 表user的操作数据库接口实现</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月05日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Service
public class UserDOServiceImpl extends ServiceImpl<UserDao, UserDO> implements UserDOService {
}