package com.ktvme.songcopyright.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.bo.SongBO;
import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import com.ktvme.songcopyright.model.entity.SongDO;
import com.ktvme.songcopyright.model.par.SongCopyrightPagePar;
import com.ktvme.songcopyright.model.par.SongPar;
import com.ktvme.songcopyright.model.par.SongImportPar;
import com.ktvme.songcopyright.model.vo.SongCopyrightVO;
import com.ktvme.songcopyright.service.CopyrightService;
import com.ktvme.songcopyright.service.dao.SongCopyrightService;
import com.ktvme.songcopyright.service.dao.SongService;
import com.ktvme.songcopyright.utils.SongExcelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 版权查询接口实现</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class CopyrightServiceImpl implements CopyrightService {

    private final SongService songService;
    private final SongCopyrightService songCopyrightService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleSongExcel(SongImportPar par) {
        log.info("[INF] -----> start analysis song excel:{}", par);
        SongExcelUtil.doRead(par.getExcelFile());

        log.info("[INF] ---> all song data now in memory");
        List<SongBO> songListOri = SongExcelUtil.getSongList();
        // 初步去重
        List<SongBO> songList = new ArrayList<>(new HashSet<>(songListOri));
        // 去除和数据库中重复的歌曲_歌手组合
        List<SongDO> nonDuplicateList = new ArrayList<>();

        for (SongBO song : songList) {
            QueryWrapper<SongDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("song_title", song.getSongTitle())
                    .eq("artist", song.getArtist());
            int count = songService.count(queryWrapper);
            if (count == 0) {
                nonDuplicateList.add(SongBO.convertSongBo2Do(song));
            }
        }
        boolean success;
        if (nonDuplicateList.isEmpty()){
            success = true;
        } else {
            success = songService.saveBatch(nonDuplicateList);
        }
        log.info("[INF] ---> song save res: {}", success);

        if (!success){
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "保存歌曲数据失败");
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<SongCopyrightVO> pageSongCopyrightResults(SongCopyrightPagePar par) {
        log.info("[INF] ----> 获取分页歌曲版权数据参数: {}", par);

        IPage<SongCopyrightDO> page = songCopyrightService.page(
                new Page<>(par.getCurrent(), par.getSize()),
                buildQueryWrapper(par)
        );

        IPage<SongCopyrightVO> res = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        res.setRecords(
                page.getRecords().stream().map(SongCopyrightVO::convertSongCopyrightDo2Vo)
                        .collect(Collectors.toList())
        );
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSongData(SongPar par) {
        log.info("[INF] ----> 新增歌曲: {}", par);

        SongDO songDo = SongDO.builder()
                .songTitle(par.getSongTitle())
                .artist(par.getArtist())
                .build();

        boolean success = songService.save(songDo);
        log.info("[INF] ---> song save res: {}", success);

        if (!success){
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "新增歌曲失败");
        }
        return Boolean.TRUE;
    }

    private LambdaQueryWrapper<SongCopyrightDO> buildQueryWrapper(SongCopyrightPagePar par) {
        LambdaQueryWrapper<SongCopyrightDO> wrapper = Wrappers.<SongCopyrightDO>query().lambda()
                .eq(SongCopyrightDO::getSongTitle, par.getSongTitle());

        wrapper.orderByAsc(SongCopyrightDO::getYear);
        return wrapper;
    }
}