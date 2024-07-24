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
import com.ktvme.songcopyright.spider.MusicSearch;
import com.ktvme.songcopyright.utils.SongExcelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleSongExcel(SongImportPar par){
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

        for (SongDO song: nonDuplicateList){
            rocketMQTemplate.convertAndSend("song-topic", song);
            log.info("[INF] ---> {} has send success", song);
        }

        return Boolean.TRUE;
    }

    @Override
    public IPage<SongCopyrightVO> pageSongCopyrightResults(SongCopyrightPagePar par) {
        log.info("[INF] ----> 获取分页歌曲版权数据参数: {}", par);

        if (par.getSongTitle().isEmpty()){
            return new Page<>(par.getCurrent(), par.getSize(), 0);
        }

        IPage<SongCopyrightDO> page = songCopyrightService.page(
                new Page<>(par.getCurrent(), par.getSize()),
                buildQueryWrapper(par)
        );

        // 如果搜索结果为空，则返回特殊状态
        if (page.getRecords().isEmpty()){
            return new Page<>(par.getCurrent(), page.getSize(), -1);
        }

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<SongCopyrightVO> pageSongCopyrightSearchResults(SongCopyrightPagePar par) {
        SongDO songDO = SongDO.builder()
                .songTitle(par.getSongTitle())
                .artist("")
                .build();
        List<SongCopyrightDO> songCopyrightWYY = MusicSearch.searchCopyrightFromWYY(songDO);
        List<SongCopyrightDO> songCopyrightQQ = MusicSearch.searchCopyrightFromQQ(songDO, 1, 10);
        // 去重
        List<SongCopyrightDO> mergedList = new ArrayList<>();
        if (songCopyrightWYY != null) mergedList.addAll(songCopyrightWYY);
        if (songCopyrightQQ != null) mergedList.addAll(songCopyrightQQ);
        if (mergedList.isEmpty()){
            log.info("[INF] ---> 搜索不到该歌曲的信息: {}", songDO.getSongTitle());
            return new Page<>(par.getCurrent(), par.getSize(), 0);
        }
        // 按照 songTitle 排序并根据 songTitle、artist 和 company 进行去重
        List<SongCopyrightDO> saveList = mergedList.stream()
                .sorted(Comparator.comparing(SongCopyrightDO::getSongTitle))
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(
                                        song -> song.getSongTitle() + song.getArtist() + song.getOriginalCompany()))),
                        ArrayList::new));
        boolean success = songCopyrightService.saveBatch(saveList);
        log.info("[INF] ---> songCopyRight save res: {}", success);

        if (!success){
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "保存歌曲版权数据失败");
        }

        IPage<SongCopyrightVO> res = new Page<>(par.getCurrent(), par.getSize(), saveList.size());
        res.setRecords(
                saveList.stream().map(SongCopyrightVO::convertSongCopyrightDo2Vo)
                        .collect(Collectors.toList())
        );
        return res;
    }

    private LambdaQueryWrapper<SongCopyrightDO> buildQueryWrapper(SongCopyrightPagePar par) {
        LambdaQueryWrapper<SongCopyrightDO> wrapper = Wrappers.<SongCopyrightDO>query().lambda()
                .like(SongCopyrightDO::getSongTitle, "%" + par.getSongTitle() + "%");

        wrapper.orderByAsc(SongCopyrightDO::getDate);
        return wrapper;
    }
}