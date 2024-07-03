package com.ktvme.songcopyright.service.dao.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktvme.songcopyright.dao.SongDataDao;
import com.ktvme.songcopyright.model.entity.SongDO;
import com.ktvme.songcopyright.service.dao.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 表song的操作数据库接口实现</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Service
public class SongServiceImpl extends ServiceImpl<SongDataDao, SongDO> implements SongService {
}